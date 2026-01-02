package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
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

        if (existingPerson.isPresent()){
            throw new ConflictException("Une personne avec le même nom et prénom a déjà été ajoutée.");
        }

        if (person.getFirstName() == null || person.getLastName() == null || person.getFirstName().isBlank() || person.getLastName().isBlank()) {
            throw new BadRequestException("Le prénom et le nom doivent être renseignés");
        }

        if (person.getAddress() == null || person.getAddress().isBlank()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }

        if (person.getCity() == null || person.getCity().isBlank() || person.getZip() == null || person.getZip().isBlank()) {
            throw new BadRequestException("La ville et le code postal doivent être renseignés.");
        }

        if (person.getPhone() == null || person.getPhone().isBlank()) {
            throw new BadRequestException("Le numéro de téléphone doit être renseigné.");
        }

        if (person.getEmail() == null || person.getEmail().isBlank()) {
            throw new BadRequestException("L'email doit être renseigné");
        }

        return personRepository.add(person);
    }

    @Override
    public boolean update(Person person) {
        if (person.getAddress() == null || person.getAddress().isBlank()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }

        if (person.getCity() == null || person.getCity().isBlank() || person.getZip() == null || person.getZip().isBlank()) {
            throw new BadRequestException("La ville et le code postal doivent être renseignés.");
        }

        if (person.getPhone() == null || person.getPhone().isBlank()) {
            throw new BadRequestException("Le numéro de téléphone doit être renseigné.");
        }

        if (person.getEmail() == null || person.getEmail().isBlank()) {
            throw new BadRequestException("L'email doit être renseigné");
        }

        return personRepository.update(person);
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        return personRepository.delete(firstName, lastName);
    }
}
