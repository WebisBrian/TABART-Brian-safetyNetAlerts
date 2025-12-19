package com.safetynetalerts.repository.medicalrecord;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation en mémoire des opérations MedicalRecord.
 */
@Repository
public class InMemoryMedicalRecordRepository implements MedicalRecordRepository {

    private final SafetyNetStore store;

    public InMemoryMedicalRecordRepository(SafetyNetStore store) {
        this.store = store;
    }

    @Override
    public List<MedicalRecord> findAll() {
        return store.read(data -> data.getMedicalrecords());
    }

    @Override
    public Optional<MedicalRecord> findByName(String firstName, String lastName) {
        return store.read(data -> data.getMedicalrecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName)
                        && mr.getLastName().equalsIgnoreCase(lastName))
                .findFirst());
    }

    @Override
    public MedicalRecord add(MedicalRecord record) {
        store.write(data -> data.getMedicalrecords().add(record));
        return record;
    }

    @Override
    public boolean update(MedicalRecord record) {
        final boolean[] updated = {false};

        store.write(data -> {
            Optional<MedicalRecord> opt = data.getMedicalrecords().stream()
                    .filter(mr -> mr.getFirstName().equalsIgnoreCase(record.getFirstName())
                            && mr.getLastName().equalsIgnoreCase(record.getLastName()))
                    .findFirst();

            if (opt.isEmpty()) {
                updated[0] = false;
                return;
            }

            MedicalRecord existing = opt.get();
            existing.setBirthdate(record.getBirthdate());
            existing.setMedications(record.getMedications());
            existing.setAllergies(record.getAllergies());

            updated[0] = true;
        });

        return updated[0];
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        final boolean[] removed = {false};

        store.write(data -> removed[0] = data.getMedicalrecords().removeIf(mr ->
                mr.getFirstName().equalsIgnoreCase(firstName)
                        && mr.getLastName().equalsIgnoreCase(lastName)));

        return removed[0];
    }
}
