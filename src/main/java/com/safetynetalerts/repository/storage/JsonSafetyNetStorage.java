package com.safetynetalerts.repository.storage;

import com.safetynetalerts.model.SafetyNetData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Stockage basé sur un fichier JSON.
 * Responsabilités :
 * 1) S'assurer qu'un fichier "modifiable" existe sur le disque (ex: ./data/data.json)
 * 2) Charger les données (JSON → SafetyNetData)
 * 3) Sauvegarder les données (SafetyNetData → JSON) de façon sûre
 * Notes :
 * - Le dossier resources est en lecture seule une fois packagé en JAR.
 * - Si le fichier existe, mais est vide, on recopie depuis resources pour éviter
 * l'erreur Jackson "end of-input".
 * - La sauvegarde utilise une écriture atomique (atomique = tout ou rien) :
 * on écrit dans un fichier temporaire puis on remplace le fichier final.
 */
@Component
public class JsonSafetyNetStorage implements SafetyNetStorage {

    private static final Logger logger = LoggerFactory.getLogger(JsonSafetyNetStorage.class);

    // Jackson : sérialisation/désérialisation JSON <-> objets Java
    private final ObjectMapper objectMapper;

    // Chemin vers le fichier JSON sur disque (ex: ./data/data.json)
    private final Path dataPath;

    public JsonSafetyNetStorage(ObjectMapper objectMapper,
                                // Lit la propriété safetynet.data.path, sinon valeur par défaut
                                @Value("${safetynet.data.path:./data/data.json}") String dataPath) {
        this.objectMapper = objectMapper;
        this.dataPath = Paths.get(dataPath);
        logger.info("JsonSafetyNetStorage configured with dataPath={}", this.dataPath);
    }

    @Override
    public SafetyNetData load() {
        long start = System.currentTimeMillis();
        try {
            logger.info("Loading data from {}", dataPath);
            // Vérifie la présence du fichier modifiable ou l'initialise si manquant/vide
            ensureWritableFileExists();

            // (Deserialization) Lecture du JSON du disque → conversion en SafetyNetData
            SafetyNetData data = objectMapper.readValue(dataPath.toFile(), SafetyNetData.class);

            long duration = System.currentTimeMillis() - start;
            if (data != null) {
                int persons = data.getPersons() != null ? data.getPersons().size() : 0;
                int firestations = data.getFirestations() != null ? data.getFirestations().size() : 0;
                int medicalrecords = data.getMedicalRecords() != null ? data.getMedicalRecords().size() : 0;
                logger.info("Loaded data (after deserialization): persons={}, firestations={}, medicalrecords={} ({} ms)", persons, firestations, medicalrecords, duration);
            } else {
                logger.warn("Loaded data (after deserialization) is null ({} ms)", duration);
            }
            logger.debug("Load completed in {} ms", duration);
            return data;

        } catch (IOException e) {
            logger.error("Failed to read JSON data from {}: {}", dataPath, e.getMessage(), e);
            // On remonte une exception non vérifiée : l'application ne peut pas fonctionner correctement
            throw new IllegalStateException("Cannot read data from " + dataPath, e);
        }
    }

    @Override
    public void save(SafetyNetData data) {
        long start = System.currentTimeMillis();
        // Écriture atomique :
        // - écrire dans un fichier temporaire
        // - déplacer le fichier temporaire vers le fichier final (remplacement)
        Path tmp = dataPath.resolveSibling(dataPath.getFileName() + ".tmp");

        try {
            logger.info("Persisting data to {}", dataPath);
            // Crée le dossier parent (./data) si nécessaire
            Files.createDirectories(dataPath.getParent());

            // Écrit l'objet Java en JSON dans le fichier temporaire
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tmp.toFile(), data);

            try {
                // Remplace le fichier final par le tmp de manière atomique si possible
                Files.move(tmp, dataPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                logger.debug("Atomic move succeeded for {}", dataPath);
            } catch (AtomicMoveNotSupportedException ex) {
                logger.debug("ATOMIC_MOVE not supported, using non-atomic move instead for {}", dataPath);
                // Certains systèmes de fichiers ne supportent pas ATOMIC_MOVE
                // → fallback : move classique (moins "robuste" mais fonctionnel).
                Files.move(tmp, dataPath, StandardCopyOption.REPLACE_EXISTING);
            }
            long duration = System.currentTimeMillis() - start;
            logger.info("Persisted data successfully ({} ms)", duration);
        } catch (IOException e) {
            logger.error("Failed to persist JSON data to {}: {}", dataPath, e.getMessage(), e);
            throw new IllegalStateException("Cannot write data to " + dataPath, e);
        } finally {
            // Nettoyage :
            // si une exception a eu lieu avant le move, on essaie de supprimer le .tmp
            try {
                Files.deleteIfExists(tmp);
            } catch (IOException ignored) {
                // échec de nettoyage : on ignore volontairement
            }
        }
    }

    /**
     * Garantit qu'un fichier modifiable existe à l'emplacement configuré (dataPath).
     * Comportement :
     * - Crée le dossier parent si nécessaire.
     * - Si le fichier est manquant ou vide (taille 0), copie le fichier par défaut "data.json"
     * depuis les resources du classpath vers l'emplacement modifiable.
     * Ce comportement évite les erreurs de lecture Jackson lorsque le fichier cible est absent
     * ou vide après le packaging.
     *
     * @throws IOException en cas d'échec d'opérations I/O (création de dossier, copie, lecture de taille…).
     */
    private void ensureWritableFileExists() throws IOException {
        // Crée le dossier parent si nécessaire
        Files.createDirectories(dataPath.getParent());

        // manquant : le fichier n'existe pas
        boolean missing = Files.notExists(dataPath);

        // vide : le fichier existe, mais sa taille est 0 octet
        boolean empty = !missing && Files.size(dataPath) == 0;

        // Si le fichier existe ET n'est pas vide, rien à faire
        if (!missing && !empty) {
            logger.debug("Writable data file exists: {}, size={} bytes", dataPath, Files.size(dataPath));
            return;
        }

        // Sinon : on copie le fichier data.json présent dans resources vers l'emplacement modifiable
        logger.info("Data file {} is {}. Copying default data.json from classpath", dataPath, missing ? "missing" : "empty");
        ClassPathResource resource = new ClassPathResource("data.json");
        try (InputStream in = resource.getInputStream()) {
            Files.copy(in, dataPath, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Copied classpath data.json to {}", dataPath);
        }
    }
}
