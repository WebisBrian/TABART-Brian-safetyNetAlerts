package com.safetynetalerts.dto.request;

import com.safetynetalerts.model.exception.BadRequestException;

/**
 * Requête d'ajout/mise à jour d'une Firestation.
 * Contient les champs attendus dans le body JSON.
 */
public record FirestationUpsertRequestDto(String address,
                                          Integer station) {

    public FirestationUpsertRequestDto validateAndNormalize() {
        if (address == null) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }
        String addr = address.trim();
        if (addr.isEmpty()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }
        if (station == null) {
            throw new BadRequestException("Le numéro de la station doit être renseigné.");
        }
        if (station <= 0) {
            throw new BadRequestException("Le numéro de la station doit être un entier strictement supérieur à zéro.");
        }

        return new FirestationUpsertRequestDto(addr, station);
    }
}