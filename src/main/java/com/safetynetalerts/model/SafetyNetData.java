package com.safetynetalerts.model;

import java.util.List;

public class SafetyNetData {

    // fields
    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;

    // constructors
    public SafetyNetData() {}

    public SafetyNetData(List<Person> persons, List<Firestation> firestations, List<MedicalRecord> medicalrecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalrecords = medicalrecords;
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

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }

    // overrides
    @Override
    public String toString() {
        return "SafetyNetData [persons=" + persons + ", firestations=" + firestations + ", medicalrecords=" + medicalrecords + "]";
    }
}
