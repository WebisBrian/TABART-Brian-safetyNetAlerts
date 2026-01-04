package com.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Représente une personne enregistrée dans le fichier de données SafetyNet.
 * Contient les informations d'identité et de contact nécessaires aux différents
 * endpoints de l'application (adresse, ville, téléphone, email...).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    // fields
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("address")
    private String address;
    @JsonProperty("city")
    private String city;
    @JsonProperty("zip")
    private String zip;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("email")
    private String email;

    // constructors
    public Person() {
    }

    public Person(String firstName, String lastName, String address, String city, String zip, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // overrides

    @Override
    public String toString() {
        return "Person{"
                + "firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", address='" + address + '\''
                + ", city='" + city + '\''
                + ", zip='" + zip + '\''
                + ", phone='" + phone + '\''
                + ", email='" + email + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
