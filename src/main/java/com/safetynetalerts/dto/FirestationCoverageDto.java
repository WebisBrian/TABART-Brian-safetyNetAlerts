package com.safetynetalerts.dto;

import java.util.List;

/**
 * Représente la réponse de l'endpoint /firestation pour un numéro de caserne donné.
 * Contient la liste des personnes desservies par cette caserne ainsi que le nombre
 * d'adultes et d'enfants parmi elles.
 */
public class FirestationCoverageDto {

    // fields
    private List<CoveredPersonDto> persons;
    private int numberOfAdults;
    private int numberOfChildren;

    // constructors
    public FirestationCoverageDto() {}

    public FirestationCoverageDto(List<CoveredPersonDto> persons, int numberOfAdults, int numberOfChildren) {
        this.persons = persons;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    // getters and setters
    public List<CoveredPersonDto> getPersons() {
        return persons;
    }

    public void setPersons(List<CoveredPersonDto> persons) {
        this.persons = persons;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    @Override
    public String toString() {
        return "FirestationCoverageDto{" +
                "persons=" + persons +
                ", numberOfAdults=" + numberOfAdults +
                ", numberOfChildren=" + numberOfChildren +
                '}';
    }
}
