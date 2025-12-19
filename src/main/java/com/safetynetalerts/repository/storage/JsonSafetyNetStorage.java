package com.safetynetalerts.repository.storage;

import com.safetynetalerts.model.SafetyNetData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Stockage basé sur un fichier JSON.
 *
 * Responsabilités :
 * 1) S'assurer qu'un fichier "modifiable" existe sur le disque (ex: ./data/data.json)
 * 2) Charger les données (JSON -> SafetyNetData)
 * 3) Sauvegarder les données (SafetyNetData -> JSON) de façon sûre
 *
 * Notes :
 * - Le dossier resources est en lecture seule une fois packagé en JAR.
 * - Si le fichier existe mais est vide, on recopie depuis resources pour éviter
 *   l'erreur Jackson "end-of-input".
 * - La sauvegarde utilise une écriture atomique (atomique = tout ou rien) :
 *   on écrit dans un fichier temporaire puis on remplace le fichier final.
 */
@Component
public class JsonSafetyNetStorage implements SafetyNetStorage {

    // Jackson : sérialisation/désérialisation JSON <-> objets Java
    private final tools.jackson.databind.ObjectMapper objectMapper;

    // Chemin vers le fichier JSON sur disque (ex: ./data/data.json)
    private final Path dataPath;

    public JsonSafetyNetStorage(
            ObjectMapper objectMapper,
            // Lit la propriété safetynet.data.path, sinon valeur par défaut
            @Value("${safetynet.data.path:./data/data.json}") String dataPath
    ) {
        this.objectMapper = objectMapper;
        this.dataPath = Paths.get(dataPath);
    }

    @Override
    public SafetyNetData load() {
        try {
            // Vérifie / initialise le fichier modifiable
            ensureWritableFileExists();

            // Lecture du JSON du disque -> conversion en SafetyNetData
            return objectMapper.readValue(dataPath.toFile(), SafetyNetData.class);
        } catch (IOException e) {
            // On remonte une exception non vérifiée : l'application ne peut pas fonctionner correctement
            throw new IllegalStateException("Cannot read data from " + dataPath, e);
        }
    }

    @Override
    public void save(SafetyNetData data) {
        // Écriture atomique :
        // 1) écrire dans un fichier temporaire
        // 2) déplacer (move) le fichier temporaire vers le fichier final (remplacement)
        Path tmp = dataPath.resolveSibling(dataPath.getFileName() + ".tmp");

        try {
            // Crée le dossier parent (./data) si nécessaire
            Files.createDirectories(dataPath.getParent());

            // Écrit l'objet Java en JSON dans le fichier temporaire
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tmp.toFile(), data);

            try {
                // Remplace le fichier final par le tmp de manière atomique si possible
                Files.move(tmp, dataPath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                // Certains systèmes de fichiers ne supportent pas ATOMIC_MOVE
                // -> fallback : move classique (moins "robuste" mais fonctionnel)
                Files.move(tmp, dataPath, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new IllegalStateException("Cannot write data to " + dataPath, e);

        } finally {
            // Nettoyage best-effort :
            // si une exception a eu lieu avant le move, on essaie de supprimer le .tmp
            try {
                Files.deleteIfExists(tmp);
            } catch (IOException ignored) {
                // échec de nettoyage : on ignore volontairement
            }
        }
    }

    private void ensureWritableFileExists() throws IOException {
        // Crée le dossier parent si nécessaire
        Files.createDirectories(dataPath.getParent());

        // missing : le fichier n'existe pas
        boolean missing = Files.notExists(dataPath);

        // empty : le fichier existe mais est vide (taille 0)
        boolean empty = !missing && Files.size(dataPath) == 0;

        // Si le fichier existe ET n'est pas vide, rien à faire
        if (!missing && !empty) {
            return;
        }

        // Sinon : on copie le data.json présent dans resources vers l'emplacement modifiable
        ClassPathResource resource = new ClassPathResource("data.json");
        try (InputStream in = resource.getInputStream()) {
            Files.copy(in, dataPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
