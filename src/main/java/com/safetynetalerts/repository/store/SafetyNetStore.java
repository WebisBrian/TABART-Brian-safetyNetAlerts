package com.safetynetalerts.repository.store;

import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.storage.SafetyNetStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Store en mémoire (in-memory) :
 * - contient le SafetyNetData chargé au démarrage
 * - centralise la synchronisation (Read/Write lock)
 * - centralise la persistance : toute écriture déclenche un save(...)
 *
 * Objectif : éviter de dupliquer le lock + save dans chaque repository.
 */
@Component
public class SafetyNetStore {

    private final SafetyNetStorage storage;

    /** Lock unique pour protéger l'accès au dataset en mémoire. */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    /** Données en mémoire, chargées 1 fois au démarrage. */
    private SafetyNetData data;

    public SafetyNetStore(SafetyNetStorage storage) {
        this.storage = storage;
    }

    @PostConstruct
    public void init() {
        lock.writeLock().lock();
        try {
            // Chargement initial depuis le stockage (fichier JSON)
            this.data = storage.load();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Accès en lecture : exécute une fonction sur les données protégées par le read lock.
     * @param reader fonction de lecture (ne doit pas modifier data)
     * @return résultat de la fonction
     */
    public <T> T read(Function<SafetyNetData, T> reader) {
        lock.readLock().lock();
        try {
            return reader.apply(data);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Accès en écriture : exécute une action qui modifie les données, puis sauvegarde sur disque.
     * @param writer action d'écriture (modifie data)
     */
    public void write(Consumer<SafetyNetData> writer) {
        lock.writeLock().lock();
        try {
            writer.accept(data);
            // Après toute modification, on persiste (CRUD survive restart)
            storage.save(data);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
