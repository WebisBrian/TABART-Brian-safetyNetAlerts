package com.safetynetalerts.repository.facade;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Façade qui conserve l'API "historique" SafetyNetDataRepository
 * pour éviter de refactorer tous les services d'un coup.
 *
 * @Primary => si d'autres beans implémentent SafetyNetDataRepository,
 * celui-ci sera choisi par défaut.
 */
@Primary
@Repository
public class SafetyNetDataRepositoryFacade implements SafetyNetDataRepository {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public SafetyNetDataRepositoryFacade(
            PersonRepository personRepository,
            FirestationRepository firestationRepository,
            MedicalRecordRepository medicalRecordRepository
    ) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    // -------------------- READ --------------------

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public List<Firestation> getAllFirestations() {
        return firestationRepository.findAll();
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    // -------------------- PERSON CRUD --------------------

    @Override
    public Optional<Person> findPerson(String firstName, String lastName) {
        return personRepository.findByName(firstName, lastName);
    }

    @Override
    public Person addPerson(Person person) {
        return personRepository.add(person);
    }

    @Override
    public boolean updatePerson(Person person) {
        return personRepository.update(person);
    }

    @Override
    public boolean deletePerson(String firstName, String lastName) {
        return personRepository.delete(firstName, lastName);
    }

    // -------------------- FIRESTATION CRUD --------------------

    @Override
    public Optional<Firestation> findFirestationByAddress(String address) {
        return firestationRepository.findByAddress(address);
    }

    @Override
    public Firestation addFirestation(Firestation firestation) {
        return firestationRepository.add(firestation);
    }

    @Override
    public boolean updateFirestation(Firestation firestation) {
        return firestationRepository.update(firestation);
    }

    @Override
    public boolean deleteFirestation(String address) {
        return firestationRepository.deleteByAddress(address);
    }

    // -------------------- MEDICAL RECORD CRUD --------------------

    @Override
    public Optional<MedicalRecord> findMedicalRecord(String firstName, String lastName) {
        return medicalRecordRepository.findByName(firstName, lastName);
    }

    @Override
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.add(medicalRecord);
    }

    @Override
    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.update(medicalRecord);
    }

    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        return medicalRecordRepository.delete(firstName, lastName);
    }
}
