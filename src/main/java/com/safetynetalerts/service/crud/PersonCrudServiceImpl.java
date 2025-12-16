package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;

public class PersonCrudServiceImpl implements PersonCrudService {

    private final SafetyNetDataRepository dataRepository;

    public PersonCrudServiceImpl(SafetyNetDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public Person create(Person person) {
        return dataRepository.addPerson(person);
    }

    @Override
    public boolean update(Person person) {
        return dataRepository.updatePerson(person);
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        return dataRepository.deletePerson(firstName, lastName);
    }
}
