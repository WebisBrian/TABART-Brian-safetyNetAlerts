package com.safetynetalerts.dto.crud.person;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Réponse de l'API pour une Person.
 * Permet de découpler l'API du modèle interne.
 */
@JsonPropertyOrder({"firstName", "lastName", "address", "city", "zip", "phone", "email"})
public record PersonResponseDto(String firstName,
                                String lastName,
                                String address,
                                String city,
                                String zip,
                                String phone,
                                String email) {

}