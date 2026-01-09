package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.ConflictException;
import com.safetynetalerts.repository.person.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonCrudServiceImpl implements PersonCrudService {

    private final PersonRepository personRepository;

    public PersonCrudServiceImpl(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person create(Person person) {

        Optional<Person> existingPerson = personRepository.findByName(person.getFirstName(), person.getLastName());

        if (existingPerson.isPresent()) {
            throw new ConflictException("Une personne avec le même nom et prénom a déjà été ajoutée.");
        }

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
