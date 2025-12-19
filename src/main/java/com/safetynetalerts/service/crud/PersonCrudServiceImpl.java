package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonCrudServiceImpl implements PersonCrudService {

    private final PersonRepository personRepository;

    public PersonCrudServiceImpl(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person create(Person person) {
        return personRepository.add(person);
    }

    @Override
    public boolean update(Person person) {
        return personRepository.update(person);
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        return personRepository.delete(firstName, lastName);
    }
}
