package com.safetynetalerts.repository;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface définissant les méthodes d'accès aux données chargées depuis le
 * fichier data.json. Elle fournit les listes complètes des personnes, des casernes
 * de pompiers et des dossiers médicaux.
 * Les implémentations concrètes (ex. JsonDataRepository) sont responsables
 * du chargement et du stockage de ces données en mémoire.
 */
@Repository
public interface SafetyNetDataRepository {

    List<Person> getAllPersons();
    List<Firestation> getAllFirestations();
    List<MedicalRecord> getAllMedicalRecords();
}
