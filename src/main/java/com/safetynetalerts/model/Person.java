package com.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder({"firstName", "lastName", "address", "city", "zip", "phone", "email"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    // Constructeur par défaut
    public Person() {
    }

    @JsonCreator
    // Constructeur privé
    private Person(
            @JsonProperty("firstName")String firstName,
            @JsonProperty("lastName")String lastName,
            @JsonProperty("address")String address,
            @JsonProperty("city")String city,
            @JsonProperty("zip") String zip,
            @JsonProperty("phone")String phone,
            @JsonProperty("email")String email
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    // Factory method (remplace le constructeur public)
    public static Person create(String firstName, String lastName, String address,
                                String city, String zip, String phone, String email) {
        return new Person(firstName, lastName, address, city, zip, phone, email);
    }

    // Méthodes métier
    public void updateContactInfo(String address, String city, String zip,
                                    String phone, String email) {
                this.address = address;
                this.city = city;
                this.zip = zip;
                this.phone = phone;
                this.email = email;
    }

    // getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return Objects.equals(firstName, person.firstName)
                && Objects.equals(lastName, person.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}


