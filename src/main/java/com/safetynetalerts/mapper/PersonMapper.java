package com.safetynetalerts.mapper;

import com.safetynetalerts.dto.response.crud.PersonResponseDto;
import com.safetynetalerts.dto.request.PersonUpsertRequestDto;
import com.safetynetalerts.model.Person;

/**
 * Mapper manuel : DTO ↔ Model.
 * Évite d'ajouter une dépendance externe (MapStruct).
 */
public final class PersonMapper {

    private PersonMapper() {
    }

    public static Person toModel(PersonUpsertRequestDto dto) {
        // DTO → modèle interne (utilisé par service/repo)
        return new Person(dto.firstName(),
                dto.lastName(),
                dto.address(),
                dto.city(),
                dto.zip(),
                dto.phone(),
                dto.email());
    }

    public static PersonResponseDto toDto(Person person) {
        // modèle interne → DTO de réponse API
        return new PersonResponseDto(person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                person.getCity(),
                person.getZip(),
                person.getPhone(),
                person.getEmail());
    }
}