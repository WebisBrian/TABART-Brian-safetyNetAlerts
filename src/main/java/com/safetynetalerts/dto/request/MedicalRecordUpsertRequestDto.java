package com.safetynetalerts.dto.request;

import java.util.List;

/**
 * Requête d'ajout/mise à jour d'un dossier médical.
 * Contient les champs attendus dans le body JSON.
 */
public record MedicalRecordUpsertRequestDto(String firstName,
                                            String lastName,
                                            String birthdate,
                                            List<String> medications,
                                            List<String> allergies) {
}