package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;

public interface PersonCrudService {

    Person create(Person person);

    boolean update(Person person);

    boolean delete(String firstName, String lastName);
}
