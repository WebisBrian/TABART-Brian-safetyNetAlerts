package com.safetynetalerts.dto.response.crud;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Réponse de l'API pour un dossier médical.
 * L'ordre des champs est imposé uniquement pour la lisibilité (Postman, debug).
 */
@JsonPropertyOrder({"firstName", "lastName", "birthdate", "medications", "allergies"})
public record MedicalRecordResponseDto(String firstName,
                                       String lastName,
                                       String birthdate,
                                       List<String> medications,
                                       List<String> allergies) {
}