package com.safetynetalerts.dto.childalert;

/**
 * Représente un autre membre du foyer pour l'endpoint /childAlert
 * (adulte ou enfant), avec prénom, nom, âge et téléphone.
 */
public class ChildAlertHouseholdMemberDto {

    // fields
    private String firstName;
    private String lastName;
    private int age;
    private String phone;

    // constructors
    public ChildAlertHouseholdMemberDto() {}

    public ChildAlertHouseholdMemberDto(String firstName, String lastName, int age, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // overrides
    @Override
    public String toString() {
        return "ChildAlertHouseholdMemberDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                '}';
    }
}
