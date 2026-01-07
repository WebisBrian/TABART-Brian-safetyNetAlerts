package com.safetynetalerts.repository.person;

import com.safetynetalerts.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * Repository dédié aux Person.
 * Ici : lecture + CRUD Person uniquement.
 */
public interface PersonRepository {

    List<Person> findAll();

    List<Person> findAllByLastName(String lastName);

    Optional<Person> findByName(String firstName, String lastName);

    Person add(Person person);

    boolean update(Person person);

    boolean delete(String firstName, String lastName);
}
