package com.safetynetalerts.dto.firestation;

/**
 * Représente une personne couverte par une caserne de pompiers pour l'endpoint /firestation.
 * Contient les informations nécessaires à la réponse : nom, prénom, adresse et téléphone.
 */
public class CoveredPersonDto {

    // fields
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    // constructors
    public CoveredPersonDto() {
    }

    public CoveredPersonDto(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }

    // getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CoveredPersonDto{"
                + "firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", address='" + address + '\''
                + ", phone='" + phone + '\''
                + '}';
    }
}
