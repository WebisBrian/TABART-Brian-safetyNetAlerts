package com.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Représente la structure racine du fichier data.json.
 * Elle regroupe l'ensemble des personnes, des casernes de pompiers et des
 * dossiers médicaux chargés au démarrage de l'application.
 * Cette classe sert de conteneur principal pour la désérialisation du fichier JSON
 * et fournit les données brutes à la couche repository.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SafetyNetData {

    // fields
    @JsonProperty("persons") private List<Person> persons;
    @JsonProperty("firestations") private List<Firestation> firestations;
    @JsonProperty("medicalrecords") private List<MedicalRecord> medicalRecords;

    // constructors
    public SafetyNetData() {}

    public SafetyNetData(List<Person> persons, List<Firestation> firestations, List<MedicalRecord> medicalRecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalRecords = medicalRecords;
    }

    // getters and setters
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
    public String toString() {
        return "SafetyNetData [persons=" + persons + ", firestations=" + firestations + ", medicalrecords=" + medicalRecords + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SafetyNetData that = (SafetyNetData) o;
        return Objects.equals(persons, that.persons) && Objects.equals(firestations, that.firestations) && Objects.equals(medicalRecords, that.medicalRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, firestations, medicalRecords);
    }
}
