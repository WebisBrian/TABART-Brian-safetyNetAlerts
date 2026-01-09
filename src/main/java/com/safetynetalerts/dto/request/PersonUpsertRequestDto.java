package com.safetynetalerts.dto.request;

import com.safetynetalerts.model.exception.BadRequestException;

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

    public PersonUpsertRequestDto validateAndNormalize() {
        String validFirstName = requireNonBlank(firstName, "Le prénom et le nom doivent être renseignés.");
        String validLastName = requireNonBlank(lastName, "Le prénom et le nom doivent être renseignés.");
        String validAddress = requireNonBlank(address, "L'adresse doit être renseignée.");
        String validCity = requireNonBlank(city, "La ville et le code postal doivent être renseignés.");
        String validZip = requireNonBlank(zip, "La ville et le code postal doivent être renseignés.");
        String validPhone = requireNonBlank(phone, "Le numéro de téléphone doit être renseigné.");
        String validEmail = requireNonBlank(email, "L'email doit être renseigné.");

        return new PersonUpsertRequestDto(validFirstName, validLastName, validAddress, validCity,
                validZip, validPhone, validEmail);
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

}