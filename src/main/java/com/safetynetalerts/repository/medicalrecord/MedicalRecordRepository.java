package com.safetynetalerts.repository.medicalrecord;

import com.safetynetalerts.model.MedicalRecord;

import java.util.List;
import java.util.Optional;

/**
 * Repository dédié aux MedicalRecord.
 * Ici : lecture + CRUD MedicalRecord uniquement.
 */
public interface MedicalRecordRepository {

    List<MedicalRecord> findAll();

    Optional<MedicalRecord> findByName(String firstName, String lastName);

    MedicalRecord add(MedicalRecord record);

    boolean update(MedicalRecord record);

    boolean delete(String firstName, String lastName);
}
