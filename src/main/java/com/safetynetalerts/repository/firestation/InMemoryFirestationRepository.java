package com.safetynetalerts.repository.firestation;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation en mémoire des opérations Firestation.
 */
@Repository
public class InMemoryFirestationRepository implements FirestationRepository {

    private final SafetyNetStore store;

    public InMemoryFirestationRepository(SafetyNetStore store) {
        this.store = store;
    }

    @Override
    public List<Firestation> findAll() {
        return store.read(SafetyNetData::getFirestations);
    }

    @Override
    public Optional<Firestation> findByAddress(String address) {
        return store.read(data -> data.getFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst());
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
