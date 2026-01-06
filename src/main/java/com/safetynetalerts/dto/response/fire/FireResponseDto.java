package com.safetynetalerts.dto.response.fire;

import com.safetynetalerts.dto.common.ResidentWithMedicalInfoDto;

import java.util.List;

/**
 * Représente une liste des résidents pour l'endpoint /fire, dont les champs sont ceux de FireResidentDto.
 * Prend en compte le numéro de caserne.
 *
 */
public record FireResponseDto(
        int stationNumber,
        List<ResidentWithMedicalInfoDto> residents
) {
}