package com.safetynetalerts.dto.request;

import com.safetynetalerts.model.exception.BadRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    private static final DateTimeFormatter BIRTHDATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public MedicalRecordUpsertRequestDto validateAndNormalize() {
        String validFirstName = requireNonBlank(firstName, "Le prénom et le nom doivent être renseignés.");
        String validLastName = requireNonBlank(lastName, "Le prénom et le nom doivent être renseignés.");
        String validBirthdate = requireNonBlank(birthdate, "La date de naissance doit être renseignée.");

        // Vérifie que la date est parseable (sinon 400)
        validateBirthdate(validBirthdate);

        List<String> validMedications = normalizeStringList(medications);
        List<String> validAllergies = normalizeStringList(allergies);

        return new MedicalRecordUpsertRequestDto(validFirstName, validLastName, validBirthdate, validMedications, validAllergies);
    }

    private static String requireNonBlank(String value, String message) {
        if (value == null) {
            throw new BadRequestException(message);
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new BadRequestException(message);
        }
        return trimmed;
    }

    private static void validateBirthdate(String birthdate) {
        try {
            LocalDate.parse(birthdate, BIRTHDATE_FORMAT);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Le format de la date de naissance est invalide (attendu : MM/jj/aaaa).");
        }
    }

    private static List<String> normalizeStringList(List<String> input) {
        if (input == null) {
            return List.of();
        }
        return input.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(String::trim)
                .distinct()
                .toList();
    }
}