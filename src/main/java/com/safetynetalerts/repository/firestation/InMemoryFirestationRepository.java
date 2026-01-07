package com.safetynetalerts.repository.firestation;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation en mémoire des opérations Firestation.
 */
@Repository
public class InMemoryFirestationRepository implements FirestationRepository {

    private final Logger logger = LoggerFactory.getLogger(InMemoryFirestationRepository.class);
    private final SafetyNetStore store;

    public InMemoryFirestationRepository(SafetyNetStore store) {
        this.store = store;
    }

    @Override
    public List<Firestation> findAll() {
        List<Firestation> firestations = store.read(SafetyNetData::getFirestations);

        return firestations != null ? firestations : Collections.emptyList();
    }

    @Override
    public Optional<Firestation> findByAddress(String address) {
        if (address == null) {
            logger.warn("findByAddress appelé avec address=null");
            return Optional.empty();
        }

        List<Firestation> firestations = firestationsExemptOfNull();

        return firestations.stream()
                .filter(f -> f != null
                && address.equalsIgnoreCase(f.getAddress()))
                .findFirst();
    }

    @Override
    public Optional<Firestation> findByAddressAndByStation(String address, Integer station) {
        if (address == null || station == null) {
            logger.warn("findByAddressAndByStation appelé avec address={} ou station=null", address);
            return Optional.empty();
        }

        List<Firestation> firestations = firestationsExemptOfNull();

        return firestations.stream()
                .filter(f -> f != null
                && address.equalsIgnoreCase(f.getAddress())
                && station.equals(f.getStation()))
                .findFirst();
    }

    private List<Firestation> firestationsExemptOfNull() {
        List<Firestation> firestations = store.read(SafetyNetData::getFirestations);
        if (firestations == null) {
            logger.error("Liste firestations est null dans SafetyNetData");
            return Collections.emptyList();
        }
        return firestations;
    }

    //    CRUD
    @Override
    public Firestation add(Firestation firestation) {
        store.write(data -> data.getFirestations().add(firestation));
        return firestation;
    }

    @Override
    public boolean update(Firestation firestation) {
        final boolean[] updated = {false};

        store.write(data -> {
            Optional<Firestation> opt = data.getFirestations().stream()
                    .filter(fs -> fs.getAddress().equalsIgnoreCase(firestation.getAddress()))
                    .findFirst();

            if (opt.isEmpty()) {
                updated[0] = false;
                return;
            }

            opt.get().setStation(firestation.getStation());
            updated[0] = true;
        });

        return updated[0];
    }

    @Override
    public boolean deleteByAddress(String address) {
        final boolean[] removed = {false};

        store.write(data -> removed[0] = data.getFirestations().removeIf(fs ->
                fs.getAddress().equalsIgnoreCase(address)));

        return removed[0];
    }
}
