package com.safetynetalerts.dto.crud.person;

import com.safetynetalerts.model.Person;

/**
 * Mapper manuel : DTO <-> Model.
 * Évite d'ajouter une dépendance externe (MapStruct).
 */
public final class PersonMapper {

    private PersonMapper() {
    }

    public static Person toModel(PersonUpsertRequestDto dto) {
        // On construit le modèle interne utilisé par le service/repo
        return new Person(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAddress(),
                dto.getCity(),
                dto.getZip(),
                dto.getPhone(),
                dto.getEmail()
        );
    }

    public static PersonResponseDto toDto(Person person) {
        // On construit la réponse API à partir du modèle interne
        PersonResponseDto dto = new PersonResponseDto();
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setCity(person.getCity());
        dto.setZip(person.getZip());
        dto.setPhone(person.getPhone());
        dto.setEmail(person.getEmail());
        return dto;
    }
}