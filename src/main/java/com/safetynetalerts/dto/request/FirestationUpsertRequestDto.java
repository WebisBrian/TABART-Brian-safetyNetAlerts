package com.safetynetalerts.dto.request;

/**
 * Requête d'ajout/mise à jour d'une Firestation.
 * Contient les champs attendus dans le body JSON.
 */
public class FirestationUpsertRequestDto {

    // fields
    private String address;
    private Integer station;

    // constructors
    public FirestationUpsertRequestDto() {
    }

    public FirestationUpsertRequestDto(String address, Integer station) {
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