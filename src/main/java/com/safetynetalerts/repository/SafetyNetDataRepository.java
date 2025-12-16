package com.safetynetalerts.repository;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * Interface définissant les méthodes d'accès aux données chargées depuis le
 * fichier data.json. Elle fournit les listes complètes des personnes, des casernes
 * de pompiers et des dossiers médicaux ainsi qu'un CRUD pour chacun d'entre eux.
 * Les implémentations concrètes (ex. JsonDataRepository) sont responsables
 * du chargement et du stockage de ces données en mémoire.
 */
public interface SafetyNetDataRepository {

    // READ
    List<Person> getAllPersons();
    List<Firestation> getAllFirestations();
    List<MedicalRecord> getAllMedicalRecords();

    // PERSON CRUD
    Optional<Person> findPerson(String firstName, String lastName);
    Person addPerson(Person person);
    boolean updatePerson(Person person);
    boolean deletePerson(String firstName, String lastName);

    // FIRESTATION CRUD
    Optional<Firestation> findFirestationByAddress(String address);
    Firestation addFirestation(Firestation firestation);
    boolean updateFirestation(Firestation firestation);
    boolean deleteFirestation(String address);

    // MEDICAL RECORD CRUD
    Optional<MedicalRecord> findMedicalRecordByPerson(String firstName, String lastName);
    MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);
    boolean updateMedicalRecord(MedicalRecord medicalRecord);
    boolean deleteMedicalRecord(String firstName, String lastName);
}
