package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.SafetyNetDataRepository;

public class MedicalRecordCrudServiceImpl implements MedicalRecordCrudService {

    private final SafetyNetDataRepository dataRepository;

    public MedicalRecordCrudServiceImpl(SafetyNetDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public MedicalRecord create(MedicalRecord medicalRecord) {
        return dataRepository.addMedicalRecord(medicalRecord);
    }

    @Override
    public boolean update(MedicalRecord medicalRecord) {
        return dataRepository.updateMedicalRecord(medicalRecord);
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        return dataRepository.deleteMedicalRecord(firstName, lastName);
    }
}
