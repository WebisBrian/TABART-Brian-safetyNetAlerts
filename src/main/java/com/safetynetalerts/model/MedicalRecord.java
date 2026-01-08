package com.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

@JsonPropertyOrder({"firstName", "lastName", "birthdate", "medications", "allergies"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    // Constructeur par défaut
    public MedicalRecord() {
    }

    @JsonCreator
    // Constructeur privé
    private MedicalRecord(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("birthdate") String birthdate,
            @JsonProperty("medications") List<String> medications,
            @JsonProperty("allergies") List<String> allergies
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    // Factory method (remplace le constructeur public)
    public static MedicalRecord create(String firstName, String lastName, String birthdate,
                                       List<String> medications, List<String> allergies) {
        return new MedicalRecord(firstName, lastName, birthdate, medications, allergies);
    }

    // Méthode métier
    public void updateMedicalRecord(String birthdate,
                                    List<String> medications, List<String> allergies) {
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    // getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    // overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalRecord that)) return false;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "MedicalRecord{"
                + "firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", birthdate='" + birthdate + '\''
                + ", medications=" + medications
                + ", allergies=" + allergies
                + '}';
    }
}
