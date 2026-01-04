package com.safetynetalerts.dto.crud.medicalrecord;

import java.util.List;

/**
 * Requête d'ajout/mise à jour d'un dossier médical.
 * Contient les champs attendus dans le body JSON.
 */
public class MedicalRecordUpsertRequestDto {

    // fields
    private String firstName;
    private String lastName;
    private String birthdate;          // format attendu : MM/dd/yyyy
    private List<String> medications;  // ex: "aznol:350mg"
    private List<String> allergies;

    // constructors
    public MedicalRecordUpsertRequestDto() {
    }

    public MedicalRecordUpsertRequestDto(String firstName, String lastName, String birthdate,
                                         List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    // getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}