package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.MedicalRecord;

public interface MedicalRecordCrudService {

    MedicalRecord create(MedicalRecord medicalRecord);
    boolean update(MedicalRecord medicalRecord);
    boolean delete(String firstName, String lastName);
}
