package com.safetynetalerts.dto.fire;

import java.util.List;

/**
 * Représente une liste des résidents pour l'endpoint /fire, dont les champs sont ceux de FireResidentDto.
 * Prend en compte le numéro de caserne.
 * */
public class FireResponseDto {

    // fields
    private int stationNumber;
    private List<FireResidentDto> residents;

    // constructors
    public FireResponseDto() {}

    public FireResponseDto(int stationNumber, List<FireResidentDto> residents) {
        this.stationNumber = stationNumber;
        this.residents = residents;
    }

    // getters and setters
    public int getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(int stationNumber) {
        this.stationNumber = stationNumber;
    }

    public List<FireResidentDto> getResidents() {
        return residents;
    }

    public void setResidents(List<FireResidentDto> residents) {
        this.residents = residents;
    }

    // overrides
    @Override
    public String toString() {
        return "FireResponseDto{" +
                "stationNumber=" + stationNumber +
                ", residents=" + residents +
                '}';
    }
}
