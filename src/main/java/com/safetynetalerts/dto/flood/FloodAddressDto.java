package com.safetynetalerts.dto.flood;

import java.util.List;

public class FloodAddressDto {

    // fields
    private String address;
    private List<FloodResidentDto> residents;

    // constructors
    public FloodAddressDto() {}

    public FloodAddressDto(String address, List<FloodResidentDto> residents) {
        this.address = address;
        this.residents = residents;
    }

    // getters / setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FloodResidentDto> getResidents() {
        return residents;
    }

    public void setResidents(List<FloodResidentDto> residents) {
        this.residents = residents;
    }

    // overrides

    @Override
    public String toString() {
        return "FloodAddressDto{" +
                "address='" + address + '\'' +
                ", residents=" + residents +
                '}';
    }
}
