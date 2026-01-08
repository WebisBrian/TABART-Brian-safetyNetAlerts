package com.safetynetalerts.dto.response.crud;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Réponse de l'API pour une Firestation.
 * L'ordre des champs est imposé pour la lisibilité
 */
@JsonPropertyOrder({"address", "station"})
public record FirestationResponseDto(String address,
                                     Integer station) {
}

