package com.safetynetalerts.dto.crud.medicalrecord;

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
        List<String> medications = dto.getMedications() != null ? dto.getMedications() : List.of();
        List<String> allergies = dto.getAllergies() != null ? dto.getAllergies() : List.of();

        return new MedicalRecord(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getBirthdate(),
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