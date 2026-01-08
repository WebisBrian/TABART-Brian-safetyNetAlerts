package com.safetynetalerts.mapper;

import com.safetynetalerts.dto.response.crud.MedicalRecordResponseDto;
import com.safetynetalerts.dto.request.MedicalRecordUpsertRequestDto;
import com.safetynetalerts.model.MedicalRecord;

import java.util.List;

/**
 * Mapper manuel : DTO ↔ Model.
 * Évite d'ajouter une dépendance externe (MapStruct).
 */
public final class MedicalRecordMapper {

    private MedicalRecordMapper() {
    }

    public static MedicalRecord toModel(MedicalRecordUpsertRequestDto dto) {
        // DTO → modèle interne (utilisé par service/repo)
        List<String> medications = dto.medications() != null ? dto.medications() : List.of();
        List<String> allergies = dto.allergies() != null ? dto.allergies() : List.of();

        return new MedicalRecord(
                dto.firstName(),
                dto.lastName(),
                dto.birthdate(),
                medications,
                allergies
        );
    }

    public static MedicalRecordResponseDto toDto(MedicalRecord model) {
        // modèle interne → DTO de réponse API
        return new MedicalRecordResponseDto(
                model.getFirstName(),
                model.getLastName(),
                model.getBirthdate(),
                model.getMedications() != null ? model.getMedications() : List.of(),
                model.getAllergies() != null ? model.getAllergies() : List.of()
        );
    }
}