package com.safetynetalerts.dto.request;

/**
 * Requête d'ajout/mise à jour d'une Firestation.
 * Contient les champs attendus dans le body JSON.
 */
public record FirestationUpsertRequestDto(String address,
                                          Integer station) {
}