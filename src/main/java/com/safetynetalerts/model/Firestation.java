package com.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 *  Représente l'association entre une adresse et un numéro de caserne de pompiers.
 *  Cette classe est utilisée par les endpoints liés aux interventions des casernes,
 *  notamment pour déterminer les habitants desservis par une station donnée.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Firestation {

    // fields
    @JsonProperty("address") private String address;
    @JsonProperty("station") private int station;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Firestation)) return false;
        Firestation that = (Firestation) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
