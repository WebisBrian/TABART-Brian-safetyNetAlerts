package com.safetynetalerts.dto.mapper;

import com.safetynetalerts.dto.response.crud.FirestationResponseDto;
import com.safetynetalerts.dto.request.FirestationUpsertRequestDto;
import com.safetynetalerts.model.Firestation;

/**
 * Mapper manuel : DTO <-> Model.
 */
public final class FirestationMapper {

    private FirestationMapper() {
    }

    public static Firestation toModel(FirestationUpsertRequestDto dto) {
        // DTO → modèle interne (utilisé par service/repo)
        return new Firestation(dto.getAddress(), dto.getStation());
    }

    public static FirestationResponseDto toDto(Firestation model) {
        // modèle interne → DTO de réponse API
        return new FirestationResponseDto(model.getAddress(), model.getStation());
    }
}