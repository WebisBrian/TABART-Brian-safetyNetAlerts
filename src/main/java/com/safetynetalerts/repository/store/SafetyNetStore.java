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
 * - contient SafetyNetData chargé au démarrage
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
     * Verrou unique utilisé pour sécuriser l’accès aux données en mémoire.
     * Rôle :
     * - Empêcher les accès concurrents dangereux lorsque plusieurs threads
     * lisent ou modifient les données en même temps.
     * Fonctionnement :
     * - Plusieurs lectures peuvent s’exécuter en parallèle.
     * - Une écriture est exclusive : pendant une modification, aucune lecture
     * ni autre écriture n’est autorisée.
     * Détail d’implémentation :
     * - Le verrou est configuré en mode équitable (fairness = true) afin d’éviter
     * qu’une écriture attende indéfiniment à cause d’un afflux continu de lectures.
     * Ce choix privilégie la prévisibilité au détriment d’un léger coût
     * en performance.
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    /**
     * Données en mémoire, chargées une fois au démarrage.
     */
    private SafetyNetData data;

    public SafetyNetStore(SafetyNetStorage storage) {
        this.storage = storage;
    }

    /**
     * Initialise le store au démarrage de l’application.
     * Cette méthode est appelée automatiquement par Spring après la création
     * du bean ({@code @PostConstruct}). Elle charge l’ensemble des données depuis
     * le stockage persistant (fichier JSON) et les conserve en mémoire pour
     * un accès rapide pendant l’exécution de l’application.
     * Le chargement est protégé par un verrou d’écriture afin de garantir
     * un accès exclusif aux données durant l’initialisation :
     * aucune lecture ni écriture concurrente n’est possible tant que
     * l’initialisation n’est pas terminée.
     * En cas d’erreur lors du chargement, une exception non vérifiée est levée
     * pour provoquer l’échec du démarrage de l’application. Celle-ci ne peut
     * pas fonctionner correctement sans des données valides.
     */
    @PostConstruct
    public void init() {
        long start = System.currentTimeMillis();
        logger.info("Initialisation du SafetyNetStore (stockage des données en mémoire)");
        lock.writeLock().lock();
        try {
            // Chargement initial depuis le stockage (fichier JSON)
            this.data = storage.load();
            long duration = System.currentTimeMillis() - start;
            int persons = data != null && data.getPersons() != null ? data.getPersons().size() : 0;
            int firestations = data.getFirestations() != null ? data.getFirestations().size() : 0;
            int medicalrecords = data.getMedicalRecords() != null ? data.getMedicalRecords().size() : 0;
            logger.info("Initialisation du SafetyNetStore avec persons={}, firestations={}, medicalrecords={} ({} ms)", persons, firestations, medicalrecords, duration);
        } catch (RuntimeException e) {
            logger.error("Échec d'initialisation du SafetyNetStore: {}", e.getMessage(), e);
            throw e;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Exécute une opération de lecture sur les données en mémoire de manière thread safe.
     * Cette méthode permet d’accéder aux données sans les modifier. L’opération de lecture
     * fournie est exécutée sous un verrou de lecture, ce qui autorise plusieurs lectures
     * concurrentes tant qu’aucune écriture n’est en cours.
     * Le code appelant fournit une fonction qui reçoit l’instance {@link SafetyNetData}
     * et retourne un résultat calculé à partir des données.
     *
     * @param reader fonction de lecture appliquée aux données (ne doit provoquer aucune modification)
     * @param <T>    type du résultat retourné par la fonction de lecture
     * @return résultat de l’opération de lecture
     */
    public <T> T read(Function<SafetyNetData, T> reader) {
        long start = System.currentTimeMillis();
        lock.readLock().lock();
        try {
            T result = reader.apply(data);
            long duration = System.currentTimeMillis() - start;
            logger.debug("Opération de lecture du SafetyNetStore complète: {} ms)", duration);
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Exécute une opération d’écriture sur les données en mémoire de manière thread safe.
     * Cette méthode permet de modifier les données protégées. L’action d’écriture fournie
     * est exécutée sous un verrou d’écriture, garantissant un accès exclusif aux données :
     * aucune lecture ni autre écriture concurrente n’est autorisée pendant la modification.
     * Une fois la modification effectuée, les données sont immédiatement sauvegardées
     * sur le stockage persistant afin de garantir la pérennité des changements
     * (les opérations CRUD survivent à un redémarrage de l’application).
     *
     * @param writer action d’écriture appliquée aux données (doit modifier {@link SafetyNetData})
     */
    public void write(Consumer<SafetyNetData> writer) {
        long start = System.currentTimeMillis();
        lock.writeLock().lock();
        try {
            writer.accept(data);
            // Après toute modification, on persiste (CRUD survit au restart).
            storage.save(data);
            long duration = System.currentTimeMillis() - start;
            logger.debug("Opération d'écriture du SafetyNetStore complète: ({} ms)", duration);
        } catch (RuntimeException e) {
            logger.error("Échec de l'opération d'écriture: {}", e.getMessage(), e);
            throw e;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
