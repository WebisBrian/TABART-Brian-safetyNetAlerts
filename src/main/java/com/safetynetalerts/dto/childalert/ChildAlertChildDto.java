package com.safetynetalerts.dto.childalert;

/**
 * Représente un enfant pour l'endpoint /childAlert, avec son prénom,
 * son nom et son âge calculé à partir de la date de naissance.
 */
public class ChildAlertChildDto {

    // fields
    private String firstName;
    private String lastName;
    private int age;

    // constructors
    public ChildAlertChildDto() {
    }

    public ChildAlertChildDto(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
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

    // overrides
    @Override
    public String toString() {
        return "ChildAlertChildDto{"
                + "firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", age=" + age
                + '}';
    }
}
