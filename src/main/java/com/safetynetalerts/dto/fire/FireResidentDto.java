package com.safetynetalerts.dto.fire;

import java.util.List;

/**
 * Représente une personne pour l'endpoint /fire, avec son prénom,
 * son nom, son téléphoné, son age, ses antécédents médicaux et ses allergies.
 */
public record FireResidentDto(
        String firstName,
        String lastName,
        String phone,
        int age,
        List<String> medications,
        List<String> allergies) {
}

