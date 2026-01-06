package com.safetynetalerts.dto.response.childalert;

/**
 * Représente un autre membre du foyer pour l'endpoint /childAlert
 * (adulte ou enfant), avec prénom, nom, âge et téléphone.
 */
public record ChildAlertHouseholdMemberDto(
        String firstName,
        String lastName,
        int age,
        String phone) {
}