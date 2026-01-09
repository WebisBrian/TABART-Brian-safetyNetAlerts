package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.model.exception.ConflictException;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordCrudServiceImpl implements MedicalRecordCrudService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordCrudServiceImpl(final MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public MedicalRecord create(MedicalRecord medicalRecord) {

        Optional<MedicalRecord> existingMedicalRecord = medicalRecordRepository.findByName(medicalRecord.getFirstName(), medicalRecord.getLastName());

        if (existingMedicalRecord.isPresent()) {
            throw new ConflictException("Un dossier médical a déjà été créé pour cette personne.");
        }

        return medicalRecordRepository.add(medicalRecord);
    }

    @Override
    public boolean update(MedicalRecord medicalRecord) {
        return medicalRecordRepository.update(medicalRecord);
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        return medicalRecordRepository.delete(firstName, lastName);
    }
}
