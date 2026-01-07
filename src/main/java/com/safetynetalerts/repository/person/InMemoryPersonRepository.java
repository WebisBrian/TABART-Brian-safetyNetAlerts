package com.safetynetalerts.repository.person;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation en mémoire des opérations Person.
 */
@Repository
public class InMemoryPersonRepository implements PersonRepository {

    private final Logger logger = LoggerFactory.getLogger(InMemoryPersonRepository.class);
    private final SafetyNetStore store;

    public InMemoryPersonRepository(SafetyNetStore store) {
        this.store = store;
    }

    @Override
    public List<Person> findAll() {
        List<Person> persons = store.read(SafetyNetData::getPersons);

        return persons != null ? persons : Collections.emptyList();
    }

    @Override
    public List<Person> findAllByLastName(String lastName) {
        if (lastName == null) {
            logger.warn("findAllByLastName appelé avec lastName=null");
            return Collections.emptyList();
        }

        List<Person> persons = personsExemptOfNull();

        return persons.stream()
                .filter(p -> p != null
                        && lastName.equalsIgnoreCase(p.getLastName()))
                .toList();
    }

    @Override
    public Optional<Person> findByName(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            logger.warn("findByName appelé avec firstName={} ou lastName=null", firstName);
            return Optional.empty();
        }

        List<Person> persons = personsExemptOfNull();

        return persons.stream()
                .filter(p -> p != null
                        && firstName.equalsIgnoreCase(p.getFirstName())
                        && lastName.equalsIgnoreCase(p.getLastName()))
                .findFirst();
    }

    private List<Person> personsExemptOfNull() {
        List<Person> persons = store.read(SafetyNetData::getPersons);
        if (persons == null) {
            logger.error("Liste persons est null dans SafetyNetData");
            return Collections.emptyList();
        }
        return persons;
    }

    //    CRUD
    @Override
    public Person add(Person person) {
        store.write(data -> data.getPersons().add(person));
        return person;
    }

    @Override
    public boolean update(Person person) {
        final boolean[] updated = {false};

        store.write(data -> {
            Optional<Person> opt = data.getPersons().stream()
                    .filter(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName())
                            && p.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst();

            if (opt.isEmpty()) {
                updated[0] = false;
                return;
            }

            Person existing = opt.get();
            existing.setAddress(person.getAddress());
            existing.setCity(person.getCity());
            existing.setZip(person.getZip());
            existing.setPhone(person.getPhone());
            existing.setEmail(person.getEmail());

            updated[0] = true;
        });

        return updated[0];
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        final boolean[] removed = {false};

        store.write(data -> removed[0] = data.getPersons().removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName)));

        return removed[0];
    }
}
