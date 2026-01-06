package com.safetynetalerts.dto.firestation;

import java.util.List;

/**
 * Représente la réponse de l'endpoint /firestation pour un numéro de caserne donné.
 * Contient la liste des personnes desservies par cette caserne ainsi que le nombre
 * d'adultes et d'enfants parmi elles.
 */
public record FirestationCoverageDto(
        List<CoveredPersonDto> persons,
        int numberOfAdults,
        int numberOfChildren) {
}
