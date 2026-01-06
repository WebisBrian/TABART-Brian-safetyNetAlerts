package com.safetynetalerts.dto.childalert;

/**
 * Représente un enfant pour l'endpoint /childAlert, avec son prénom,
 * son nom et son âge calculé à partir de la date de naissance.
 */
public record ChildAlertChildDto(
        String firstName,
        String lastName,
        int age) {
}
