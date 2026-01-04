package com.safetynetalerts.dto.personinfo;

import java.util.List;

public class PersonInfoResponseDto {

    // fields
    private List<PersonInfoDto> persons;

    // constructors
    public PersonInfoResponseDto() {
    }

    public PersonInfoResponseDto(List<PersonInfoDto> persons) {
        this.persons = persons;
    }

    // getters and setters
    public List<PersonInfoDto> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonInfoDto> persons) {
        this.persons = persons;
    }

    // overrides
    @Override
    public String toString() {
        return "PersonInfoResponseDto{"
                + "persons=" + persons
                + '}';
    }
}
