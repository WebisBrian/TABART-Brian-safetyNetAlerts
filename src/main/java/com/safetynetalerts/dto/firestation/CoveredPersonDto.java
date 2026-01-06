package com.safetynetalerts.dto.firestation;

/**
 * Représente une personne couverte par une caserne de pompiers pour l'endpoint /firestation.
 * Contient les informations nécessaires à la réponse : nom, prénom, adresse et téléphone.
 */
public record CoveredPersonDto(String firstName,
                               String lastName,
                               String address,
                               String phone) {
}
