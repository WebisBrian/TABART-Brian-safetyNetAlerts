package com.safetynetalerts.repository.person;

import com.safetynetalerts.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * Repository dédié aux Person (principe ISP : interface petite et ciblée).
 * Ici : lecture + CRUD Person uniquement.
 */
public interface PersonRepository {

    List<Person> findAll();

    Optional<Person> findByName(String firstName, String lastName);

    Person add(Person person);

    boolean update(Person person);

    boolean delete(String firstName, String lastName);
}
