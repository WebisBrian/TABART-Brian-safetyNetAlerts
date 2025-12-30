package com.safetynetalerts.repository.store;

import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.storage.SafetyNetStorage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Store en mémoire (in-memory) :
 * - contient le SafetyNetData chargé au démarrage
 * - centralise la synchronisation (Read/Write lock)
 * - centralise la persistance : toute écriture déclenche un save(...)
 * <p>
 * Objectif : éviter de dupliquer le lock + save dans chaque repository.
 */
@Component
public class SafetyNetStore {

    private static final Logger logger = LoggerFactory.getLogger(SafetyNetStore.class);

    private final SafetyNetStorage storage;

    /**
     * Lock unique pour protéger l'accès au dataset en mémoire.
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    /**
     * Données en mémoire, chargées 1 fois au démarrage.
     */
    private SafetyNetData data;

    public SafetyNetStore(SafetyNetStorage storage) {
        this.storage = storage;
    }

    @PostConstruct
    public void init() {
        long start = System.currentTimeMillis();
        logger.info("Initializing store");
        lock.writeLock().lock();
        try {
            // Chargement initial depuis le stockage (fichier JSON)
            this.data = storage.load();
            long duration = System.currentTimeMillis() - start;
            int persons = data != null && data.getPersons() != null ? data.getPersons().size() : 0;
            int firestations = data.getFirestations() != null ? data.getFirestations().size() : 0;
            int medicalrecords = data.getMedicalrecords() != null ? data.getMedicalrecords().size() : 0;
            logger.info("Initialized store with persons={}, firestations={}, medicalrecords={} ({} ms)", persons, firestations, medicalrecords, duration);
        } catch (RuntimeException e) {
            logger.error("Failed to initialize store: {}", e.getMessage(), e);
            throw e;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Accès en lecture : exécute une fonction sur les données protégées par le read lock.
     *
     * @param reader fonction de lecture (ne doit pas modifier data)
     * @return résultat de la fonction
     */
    public <T> T read(Function<SafetyNetData, T> reader) {
        long start = System.currentTimeMillis();
        lock.readLock().lock();
        try {
            T result = reader.apply(data);
            long duration = System.currentTimeMillis() - start;
            logger.debug("Read operation completed ({} ms)", duration);
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Accès en écriture : exécute une action qui modifie les données, puis sauvegarde sur disque.
     *
     * @param writer action d'écriture (modifie data)
     */
    public void write(Consumer<SafetyNetData> writer) {
        long start = System.currentTimeMillis();
        lock.writeLock().lock();
        try {
            writer.accept(data);
            // Après toute modification, on persiste (CRUD survive restart)
            storage.save(data);
            long duration = System.currentTimeMillis() - start;
            logger.debug("Write operation completed ({} ms)", duration);
        } catch (RuntimeException e) {
            logger.error("Write operation failed: {}", e.getMessage(), e);
            throw e;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
