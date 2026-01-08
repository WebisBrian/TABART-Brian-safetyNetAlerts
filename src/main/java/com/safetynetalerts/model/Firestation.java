package com.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder({"address", "station"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Firestation {

    private String address;
    private Integer station;

    // Constructeur par défaut
    public Firestation() {
    }

   @JsonCreator
   // Constructeur privé
    private Firestation(
            @JsonProperty("address") String address,
            @JsonProperty("station") Integer station) {
        this.address = address;
        this.station = station;
    }

    // Factory method (remplace le constructeur public)
    public static Firestation create(String address, Integer station) {
        return new Firestation(address, station);
    }

    // Méthodes métier
    public void updateFirestation(Integer station) {
        this.station = station;
    }

    // getters
    public String getAddress() {
        return address;
    }

    public Integer getStation() {
        return station;
    }

    // overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Firestation that)) return false;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "Firestation{"
                + "address='" + address + '\''
                + ", station=" + station
                + '}';
    }
}
