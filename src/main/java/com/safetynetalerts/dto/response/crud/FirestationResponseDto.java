package com.safetynetalerts.dto.response.crud;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Réponse de l'API pour une Firestation.
 * L'ordre des champs est imposé pour la lisibilité
 */
@JsonPropertyOrder({"address", "station"})
public class FirestationResponseDto {

    // fields
    private String address;
    private Integer station;

    // constructors
    public FirestationResponseDto() {
    }

    public FirestationResponseDto(String address, Integer station) {
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

    public Integer getStation() {
        return station;
    }

    public void setStation(Integer station) {
        this.station = station;
    }
}

