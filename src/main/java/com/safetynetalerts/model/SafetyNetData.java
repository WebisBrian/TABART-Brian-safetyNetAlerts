package com.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

/**
 * Représente la structure racine du fichier data.json.
 * Elle regroupe l'ensemble des personnes, des casernes de pompiers et des
 * dossiers médicaux chargés au démarrage de l'application.
 * Cette classe sert de conteneur principal pour la désérialisation du fichier JSON
 * et fournit les données brutes à la couche repository.
 */
@JsonPropertyOrder({"persons", "firestations", "medicalrecords"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SafetyNetData {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalRecords;

    // Constructeur par défaut
    public SafetyNetData() {
    }

    @JsonCreator
    // Constructeur privé
    private SafetyNetData(
            @JsonProperty("persons") List<Person> persons,
            @JsonProperty("firestations") List<Firestation> firestations,
            @JsonProperty("medicalrecords") List<MedicalRecord> medicalRecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalRecords = medicalRecords;
    }

    // Factory method (remplace le constructeur public)
    public static SafetyNetData create(List<Person> persons, List<Firestation> firestations,
                                       List<MedicalRecord> medicalRecords) {
        return new SafetyNetData(persons, firestations, medicalRecords);
    }

    // Méthodes métier
    public void updateSafetyNetData(List<Person> persons, List<Firestation> firestations,
                                    List<MedicalRecord> medicalRecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalRecords = medicalRecords;
    }

    // getters
    public List<Person> getPersons() {
        return persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    // overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SafetyNetData that)) return false;
        return Objects.equals(persons, that.persons) && Objects.equals(firestations, that.firestations) && Objects.equals(medicalRecords, that.medicalRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, firestations, medicalRecords);
    }

    @Override
    public String toString() {
        return "SafetyNetData [persons=" + persons
                + ", firestations=" + firestations
                + ", medicalrecords=" + medicalRecords
                + "]";
    }
}
