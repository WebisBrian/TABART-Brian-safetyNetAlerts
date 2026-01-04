package com.safetynetalerts.repository.person;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation en mémoire des opérations Person.
 */
@Repository
public class InMemoryPersonRepository implements PersonRepository {

    private final SafetyNetStore store;

    public InMemoryPersonRepository(SafetyNetStore store) {
        this.store = store;
    }

    @Override
    public List<Person> findAll() {
        return store.read(SafetyNetData::getPersons);
    }

    @Override
    public Optional<Person> findByName(String firstName, String lastName) {
        return store.read(data -> data.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst());
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
