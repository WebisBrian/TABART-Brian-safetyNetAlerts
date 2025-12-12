package com.safetynetalerts.model;

/**
 *  Représente l'association entre une adresse et un numéro de caserne de pompiers.
 *  Cette classe est utilisée par les endpoints liés aux interventions des casernes,
 *  notamment pour déterminer les habitants desservis par une station donnée.
 */
public class Firestation {

    // fields
    private String address;
    private int station;

    // constructors
    public Firestation() {}

    public Firestation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    // getters and setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    // overrides
    @Override
    public String toString() {
        return "Firestation{" +
                "address='" + address + '\'' +
                ", station=" + station +
                '}';
    }
}
