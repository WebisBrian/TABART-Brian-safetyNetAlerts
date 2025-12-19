package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordCrudServiceImpl implements MedicalRecordCrudService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordCrudServiceImpl(final MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public MedicalRecord create(MedicalRecord medicalRecord) {
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
