package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.model.exception.ConflictException;
import com.safetynetalerts.repository.person.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonCrudServiceImpl implements PersonCrudService {

    private static final String MSG_NAME_REQUIRED = "Le prénom et le nom doivent être renseignés.";
    private static final String MSG_ADDRESS_REQUIRED = "L'adresse doit être renseignée.";
    private static final String MSG_CITY_REQUIRED = "La ville et le code postal doivent être renseignés.";
    private static final String MSG_PHONE_REQUIRED = "Le numéro de téléphone doit être renseigné.";
    private static final String MSG_EMAIL_REQUIRED = "L'email doit être renseigné.";
    private static final String MSG_CONFLICT = "Une personne avec le même nom et prénom a déjà été ajoutée.";

    private final PersonRepository personRepository;

    public PersonCrudServiceImpl(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person create(Person person) {

        Optional<Person> existingPerson = personRepository.findByName(person.getFirstName(), person.getLastName());

        if (existingPerson.isPresent()) {
            throw new ConflictException(MSG_CONFLICT);
        }

        if (person.getFirstName() == null || person.getLastName() == null || person.getFirstName().isBlank() || person.getLastName().isBlank()) {
            throw new BadRequestException(MSG_NAME_REQUIRED);
        }

        validate(person);

        return personRepository.add(person);
    }

    @Override
    public boolean update(Person person) {
        validate(person);

        return personRepository.update(person);
    }


    @Override
    public boolean delete(String firstName, String lastName) {
        return personRepository.delete(firstName, lastName);
    }

    private void validate(Person person) {
        if (person.getAddress() == null || person.getAddress().isBlank()) {
            throw new BadRequestException(MSG_ADDRESS_REQUIRED);
        }

        if (person.getCity() == null || person.getCity().isBlank() || person.getZip() == null || person.getZip().isBlank()) {
            throw new BadRequestException(MSG_CITY_REQUIRED);
        }

        if (person.getPhone() == null || person.getPhone().isBlank()) {
            throw new BadRequestException(MSG_PHONE_REQUIRED);
        }

        if (person.getEmail() == null || person.getEmail().isBlank()) {
            throw new BadRequestException(MSG_EMAIL_REQUIRED);
        }
    }
}
