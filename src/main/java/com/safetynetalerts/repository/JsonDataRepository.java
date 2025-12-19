package com.safetynetalerts.repository;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.storage.SafetyNetStorage;
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
 *
 * Le fichier est lu une seule fois au démarrage de l'application, puis son
 * contenu est conservé en mémoire dans un objet {@link SafetyNetData}.
 * Les méthodes d'accès renvoient ensuite ces données brutes aux couches supérieures.
 */
@Repository
public class JsonDataRepository implements SafetyNetDataRepository {

    private final SafetyNetStorage storage;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private SafetyNetData data;

    public JsonDataRepository(SafetyNetStorage storage) {
        this.storage = storage;
    }

    @PostConstruct
    void init() {
        lock.writeLock().lock();
        try {
            this.data = storage.load();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // -------------------- READ --------------------

    @Override
    public List<Person> getAllPersons() {
        lock.readLock().lock();
        try {
            return data.getPersons();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Firestation> getAllFirestations() {
        lock.readLock().lock();
        try {
            return data.getFirestations();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        lock.readLock().lock();
        try {
            return data.getMedicalrecords();
        } finally {
            lock.readLock().unlock();
        }
    }

    // -------------------- PERSON CRUD --------------------

    @Override
    public Optional<Person> findPerson(String firstName, String lastName) {
        lock.readLock().lock();
        try {
            return data.getPersons().stream()
                    .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                            && p.getLastName().equalsIgnoreCase(lastName))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Person addPerson(Person person) {
        lock.writeLock().lock();
        try {
            data.getPersons().add(person);
            storage.save(data);
            return person;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean updatePerson(Person person) {
        lock.writeLock().lock();
        try {
            Optional<Person> opt = data.getPersons().stream()
                    .filter(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName())
                            && p.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst();

            if (opt.isEmpty()) return false;

            Person existing = opt.get();
            existing.setAddress(person.getAddress());
            existing.setCity(person.getCity());
            existing.setZip(person.getZip());
            existing.setPhone(person.getPhone());
            existing.setEmail(person.getEmail());

            storage.save(data);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deletePerson(String firstName, String lastName) {
        lock.writeLock().lock();
        try {
            boolean removed = data.getPersons().removeIf(p ->
                    p.getFirstName().equalsIgnoreCase(firstName)
                            && p.getLastName().equalsIgnoreCase(lastName));
            if (removed) storage.save(data);
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    // -------------------- FIRESTATION CRUD --------------------

    @Override
    public Optional<Firestation> findFirestationByAddress(String address) {
        lock.readLock().lock();
        try {
            return data.getFirestations().stream()
                    .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Firestation addFirestation(Firestation firestation) {
        lock.writeLock().lock();
        try {
            data.getFirestations().add(firestation);
            storage.save(data);
            return firestation;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean updateFirestation(Firestation firestation) {
        lock.writeLock().lock();
        try {
            Optional<Firestation> opt = data.getFirestations().stream()
                    .filter(fs -> fs.getAddress().equalsIgnoreCase(firestation.getAddress()))
                    .findFirst();

            if (opt.isEmpty()) return false;

            opt.get().setStation(firestation.getStation());
            storage.save(data);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteFirestation(String address) {
        lock.writeLock().lock();
        try {
            boolean removed = data.getFirestations().removeIf(fs ->
                    fs.getAddress().equalsIgnoreCase(address));
            if (removed) storage.save(data);
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    // -------------------- MEDICALRECORD CRUD --------------------

    @Override
    public Optional<MedicalRecord> findMedicalRecord(String firstName, String lastName) {
        lock.readLock().lock();
        try {
            return data.getMedicalrecords().stream()
                    .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName)
                            && mr.getLastName().equalsIgnoreCase(lastName))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public MedicalRecord addMedicalRecord(MedicalRecord record) {
        lock.writeLock().lock();
        try {
            data.getMedicalrecords().add(record);
            storage.save(data);
            return record;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean updateMedicalRecord(MedicalRecord record) {
        lock.writeLock().lock();
        try {
            Optional<MedicalRecord> opt = data.getMedicalrecords().stream()
                    .filter(mr -> mr.getFirstName().equalsIgnoreCase(record.getFirstName())
                            && mr.getLastName().equalsIgnoreCase(record.getLastName()))
                    .findFirst();

            if (opt.isEmpty()) return false;

            MedicalRecord existing = opt.get();
            existing.setBirthdate(record.getBirthdate());
            existing.setMedications(record.getMedications());
            existing.setAllergies(record.getAllergies());

            storage.save(data);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        lock.writeLock().lock();
        try {
            boolean removed = data.getMedicalrecords().removeIf(mr ->
                    mr.getFirstName().equalsIgnoreCase(firstName)
                            && mr.getLastName().equalsIgnoreCase(lastName));
            if (removed) storage.save(data);
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
