package com.safetynetalerts.dto.crud.medicalrecord;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Réponse de l'API pour un dossier médical.
 * L'ordre des champs est imposé uniquement pour la lisibilité (Postman, debug).
 */
@JsonPropertyOrder({ "firstName", "lastName", "birthdate", "medications", "allergies" })
public class MedicalRecordResponseDto {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    // constructors
    public MedicalRecordResponseDto() {}

    public MedicalRecordResponseDto(String firstName, String lastName, String birthdate,
                                    List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    // getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public List<String> getMedications() { return medications; }
    public void setMedications(List<String> medications) { this.medications = medications; }

    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }
}