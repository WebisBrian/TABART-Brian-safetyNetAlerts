package com.safetynetalerts.dto.childalert;

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