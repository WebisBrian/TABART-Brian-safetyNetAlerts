package com.safetynetalerts.repository;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.storage.SafetyNetStorage;
import com.safetynetalerts.repository.store.SafetyNetStore;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implémentation de {@link SafetyNetDataRepository} qui charge les données
 * de l'application à partir du fichier JSON "data.json" situé dans le classpath.
 * <p>
 * Le fichier est lu une seule fois au démarrage de l'application, puis son
 * contenu est conservé en mémoire dans un objet {@link SafetyNetData}.
 * Les méthodes d'accès renvoient ensuite ces données brutes aux couches supérieures.
 */
@Repository
public class JsonDataRepository implements SafetyNetDataRepository {

    private final SafetyNetStore store;

    public JsonDataRepository(SafetyNetStore store) {
        this.store = store;
    }

    // -------------------- READ --------------------

    @Override
    public List<Person> getAllPersons() {
        return store.read(SafetyNetData::getPersons);
    }

    @Override
    public List<Firestation> getAllFirestations() {
        return store.read(SafetyNetData::getFirestations);
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return store.read(SafetyNetData::getMedicalrecords);
    }

    // -------------------- PERSON CRUD --------------------

    @Override
    public Optional<Person> findPerson(String firstName, String lastName) {
        return store.read(data -> data.getPersons().stream().filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName)).findFirst());
    }

    @Override
    public Person addPerson(Person person) {
        // Toute écriture passe par store.write(...) => sauvegarde automatique
        store.write(data -> data.getPersons().add(person));
        return person;
    }

    @Override
    public boolean updatePerson(Person person) {
        // On a besoin d'un booléen => on calcule "updated" dans un tableau 1 élément (mutable)
        final boolean[] updated = {false};

        store.write(data -> {
            Optional<Person> opt = data.getPersons().stream().filter(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName()) && p.getLastName().equalsIgnoreCase(person.getLastName())).findFirst();

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
    public boolean deletePerson(String firstName, String lastName) {
        final boolean[] removed = {false};

        store.write(data -> removed[0] = data.getPersons().removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName)));

        return removed[0];
    }

    // -------------------- FIRESTATION CRUD --------------------

    @Override
    public Optional<Firestation> findFirestationByAddress(String address) {
        return store.read(data -> data.getFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst());
    }

    @Override
    public Firestation addFirestation(Firestation firestation) {
        store.write(data -> data.getFirestations().add(firestation));
        return firestation;
    }

    @Override
    public boolean updateFirestation(Firestation firestation) {
        final boolean[] updated = {false};

        store.write(data -> {
            Optional<Firestation> opt = data.getFirestations().stream()
                    .filter(fs -> fs.getAddress().equalsIgnoreCase(firestation.getAddress()))
                    .findFirst();

            if (opt.isEmpty()) {
                updated[0] = false;
                return;
            }

            opt.get().setStation(firestation.getStation());
            updated[0] = true;
        });

        return updated[0];
    }

    @Override
    public boolean deleteFirestation(String address) {
        final boolean[] removed = {false};

        store.write(data -> removed[0] = data.getFirestations().removeIf(fs ->
                fs.getAddress().equalsIgnoreCase(address)));

        return removed[0];
    }

    // -------------------- MEDICALRECORD CRUD --------------------

    @Override
    public Optional<MedicalRecord> findMedicalRecord(String firstName, String lastName) {
        return store.read(data -> data.getMedicalrecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName)
                        && mr.getLastName().equalsIgnoreCase(lastName))
                .findFirst());
    }

    @Override
    public MedicalRecord addMedicalRecord(MedicalRecord record) {
        store.write(data -> data.getMedicalrecords().add(record));
        return record;
    }

    @Override
    public boolean updateMedicalRecord(MedicalRecord record) {
        final boolean[] updated = {false};

        store.write(data -> {
            Optional<MedicalRecord> opt = data.getMedicalrecords().stream()
                    .filter(mr -> mr.getFirstName().equalsIgnoreCase(record.getFirstName())
                            && mr.getLastName().equalsIgnoreCase(record.getLastName()))
                    .findFirst();

            if (opt.isEmpty()) {
                updated[0] = false;
                return;
            }

            MedicalRecord existing = opt.get();
            existing.setBirthdate(record.getBirthdate());
            existing.setMedications(record.getMedications());
            existing.setAllergies(record.getAllergies());

            updated[0] = true;
        });

        return updated[0];
    }

    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        final boolean[] removed = {false};

        store.write(data -> removed[0] = data.getMedicalrecords().removeIf(mr ->
                mr.getFirstName().equalsIgnoreCase(firstName)
                        && mr.getLastName().equalsIgnoreCase(lastName)));

        return removed[0];
    }
}
