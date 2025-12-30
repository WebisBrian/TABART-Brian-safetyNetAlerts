package com.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Représente la structure racine du fichier data.json.
 * Elle regroupe l'ensemble des personnes, des casernes de pompiers et des
 * dossiers médicaux chargés au démarrage de l'application.
 * Cette classe sert de conteneur principal pour la désérialisation du fichier JSON
 * et fournit les données brutes à la couche repository.
 */
public class SafetyNetData {

    // fields
    private List<Person> persons;
    private List<Firestation> firestations;
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

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<Firestation> firestations) {
        this.firestations = firestations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    // overrides
    @Override
    public String toString() {
        return "SafetyNetData [persons=" + persons + ", firestations=" + firestations + ", medicalrecords=" + medicalRecords + "]";
    }
}
