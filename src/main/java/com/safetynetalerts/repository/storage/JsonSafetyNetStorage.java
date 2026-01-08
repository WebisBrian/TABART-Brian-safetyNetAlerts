package com.safetynetalerts.repository.storage;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Firestation;
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
import java.util.List;


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
    private final ObjectMapper objectMapper;

    // Chemin vers le fichier JSON sur disque (ex: ./data/data.json)
    private final Path dataPath;

    public JsonSafetyNetStorage(ObjectMapper objectMapper,
                                // Lit la propriété safetynet.data.path, sinon valeur par défaut
                                @Value("${safetynet.data.path:./data/data.json}") String dataPath) {
        this.objectMapper = objectMapper;
        this.dataPath = Paths.get(dataPath);
        logger.info("JsonSafetyNetStorage configuré avec dataPath={}", this.dataPath);
    }

    /**
     * Charge SafetyNetData depuis le fichier JSON configuré.
     * Si le fichier est manquant (ou vide), il doit être initialisé depuis data.json présent dans le classpath.
     */
    @Override
    public SafetyNetData load() {
        long start = System.currentTimeMillis();
        try {
            logger.info("Chargement des données depuis {}", dataPath);
            ensureWritableFileExists();
            // (Désérialisation) Lecture du JSON du disque → conversion en SafetyNetData
            SafetyNetData data = objectMapper.readValue(dataPath.toFile(), SafetyNetData.class);
            validateAndSanitize(data, start);
            return data;
        } catch (IOException e) {
            logger.error("Échec de lecture des données JSON depuis {}: {}", dataPath, e.getMessage(), e);
            // On remonte une exception non vérifiée : l'application ne peut pas fonctionner correctement
            throw new IllegalStateException("Impossible de lire les données depuis " + dataPath, e);
        }
    }

    /**
     * Enregistre SafetyNetData dans le fichier JSON configuré (écriture atomique si possible).
     */
    @Override
    public void save(SafetyNetData data) {
        long start = System.currentTimeMillis();
        // Écriture atomique :
        // - écrire dans un fichier temporaire
        // - déplacer le fichier temporaire vers le fichier final (remplacement)
        Path tmp = dataPath.resolveSibling(dataPath.getFileName() + ".tmp");

        try {
            logger.info("Persistence des données vers {}", dataPath);
            // Crée le dossier parent (./data) si nécessaire
            Files.createDirectories(dataPath.getParent());

            // Écrit l'objet Java en JSON dans le fichier temporaire
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tmp.toFile(), data);

            try {
                // Remplace le fichier final par le tmp de manière atomique si possible
                Files.move(tmp, dataPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                logger.debug("Succés du ATOMIC_MOVE pour {}", dataPath);
            } catch (AtomicMoveNotSupportedException ex) {
                // Certains systèmes de fichiers ne supportent pas ATOMIC_MOVE
                // → fallback : move classique
                logger.debug("ATOMIC_MOVE non supporté, exécution d'un mouvement 'non-atomic' pour {}", dataPath);
                Files.move(tmp, dataPath, StandardCopyOption.REPLACE_EXISTING);
            }
            long duration = System.currentTimeMillis() - start;
            logger.info("Succès de la persistence des données en ({} ms)", duration);
        } catch (IOException e) {
            logger.error("Échec de la persistence des données vers {}: {}", dataPath, e.getMessage(), e);
            throw new IllegalStateException("Impossible d'écrire les données vers " + dataPath, e);
        } finally {
            // Nettoyage :
            // si une exception a eu lieu avant le move, on essaie de supprimer le .tmp
            try {
                Files.deleteIfExists(tmp);
            } catch (IOException ignored) {
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
            logger.debug("Fichier d'écriture des données existant: {}, poids={} bytes", dataPath, Files.size(dataPath));
            return;
        }

        // Sinon : on copie le fichier data.json présent dans resources vers l'emplacement modifiable
        logger.info("Fichier de données {} est {}. Copie du fichier par défaut data.json depuis classpath", dataPath, missing ? "manquant" : "vide");
        ClassPathResource resource = new ClassPathResource("data.json");
        try (InputStream in = resource.getInputStream()) {
            Files.copy(in, dataPath, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Classpath data.json copié vers {}", dataPath);
        }
    }

    private void validateAndSanitize(SafetyNetData data, long start) {
        long duration = System.currentTimeMillis() - start;
        if (data != null) {
            int persons = validateAndSanitizePersons(data.getPersons());
            int medicalRecords = validateAndSanitizeMedicalRecords(data.getMedicalRecords());
            int firestations = validateAndSanitizeFirestations(data.getFirestations());
            logger.info("Chargement des données (après désérialisation): persons={}, firestations={}, medicalrecords={} ({} ms)", persons, firestations, medicalRecords, duration);
        } else {
            logger.warn("Chargement des données (après désérialisation) est null ({} ms)", duration);
        }
        logger.debug("Chargement complété en {} ms", duration);

    }

    private int validateAndSanitizePersons(List<Person> persons) {
        int count = persons.size();
        persons.removeIf(p -> p == null
                || p.getFirstName() == null
                || p.getLastName() == null
                || p.getAddress() == null
                || p.getCity() == null
                || p.getZip() == null
                || p.getPhone() == null
                || p.getEmail() == null
        );
        return count - persons.size();
    }

    private int validateAndSanitizeFirestations(List<Firestation> firestations) {
        int count = firestations.size();
        firestations.removeIf(f -> f == null
                || f.getAddress() == null
                || f.getStation() == null);
        return count - firestations.size();
    }

    private int validateAndSanitizeMedicalRecords(List<MedicalRecord> medicalRecords) {
        int count = medicalRecords.size();
        medicalRecords.removeIf(mr -> mr == null
                || mr.getFirstName() == null
                || mr.getLastName() == null
                || mr.getBirthdate() == null);
        return count - medicalRecords.size();
    }
}
