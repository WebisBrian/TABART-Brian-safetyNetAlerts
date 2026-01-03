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
            throw new ConflictException("Un dossier médical a déjà été créé pour cette personne");
        }

        if (medicalRecord.getFirstName() == null || medicalRecord.getLastName() == null || medicalRecord.getFirstName().isBlank() || medicalRecord.getLastName().isBlank()) {
            throw new BadRequestException("Le prénom et le nom doivent être renseignés");
        }

        if (medicalRecord.getBirthdate() == null || medicalRecord.getBirthdate().isBlank()) {
            throw new BadRequestException("La date de naissance doit être renseignée");
        }

        return medicalRecordRepository.add(medicalRecord);
    }

    @Override
    public boolean update(MedicalRecord medicalRecord) {

        if (medicalRecord.getBirthdate() == null || medicalRecord.getBirthdate().isBlank()) {
            throw new BadRequestException("La date de naissance doit être renseignée");
        }

        return medicalRecordRepository.update(medicalRecord);
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        return medicalRecordRepository.delete(firstName, lastName);
    }
}
