package com.safetynetalerts.repository.medicalrecord;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation en mémoire des opérations MedicalRecord.
 */
@Repository
public class InMemoryMedicalRecordRepository implements MedicalRecordRepository {

    private final Logger logger = LoggerFactory.getLogger(InMemoryMedicalRecordRepository.class);
    private final SafetyNetStore store;

    public InMemoryMedicalRecordRepository(SafetyNetStore store) {
        this.store = store;
    }

    @Override
    public List<MedicalRecord> findAll() {
        List<MedicalRecord> medicalRecords = store.read(SafetyNetData::getMedicalRecords);

        return medicalRecords != null ? medicalRecords : Collections.emptyList();
    }

    @Override
    public Optional<MedicalRecord> findByName(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            logger.warn("findByName appelé avec firstName={} ou lastName=null", firstName);
            return Optional.empty();
        }

        List<MedicalRecord> medicalRecords = store.read(SafetyNetData::getMedicalRecords);
        if (medicalRecords == null) {
            logger.error("Liste medicalRecords est null dans SafetyNetData");
            return Optional.empty();
        }

        return medicalRecords.stream()
                .filter(mr -> mr != null
                        && firstName.equalsIgnoreCase(mr.getFirstName())
                        && lastName.equalsIgnoreCase(mr.getLastName()))
                .findFirst();
    }

    // CRUD
    @Override
    public MedicalRecord add(MedicalRecord record) {
        store.write(data -> data.getMedicalRecords().add(record));
        return record;
    }

    @Override
    public boolean update(MedicalRecord record) {
        final boolean[] updated = {false};

        store.write(data -> {
            Optional<MedicalRecord> opt = data.getMedicalRecords().stream().filter(mr -> mr.getFirstName().equalsIgnoreCase(record.getFirstName()) && mr.getLastName().equalsIgnoreCase(record.getLastName())).findFirst();

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

        store.write(data -> removed[0] = data.getMedicalRecords().removeIf(mr -> mr.getFirstName().equalsIgnoreCase(firstName) && mr.getLastName().equalsIgnoreCase(lastName)));

        return removed[0];
    }
}
