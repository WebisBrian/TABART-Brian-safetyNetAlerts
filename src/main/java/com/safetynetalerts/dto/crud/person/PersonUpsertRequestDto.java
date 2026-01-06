package com.safetynetalerts.dto.crud.person;

/**
 * Requête d'ajout/mise à jour d'une Person.
 * Contient les champs attendus dans le body JSON.
 */
public record PersonUpsertRequestDto(String firstName,
                                     String lastName,
                                     String address,
                                     String city,
                                     String zip,
                                     String phone,
                                     String email
) {

}