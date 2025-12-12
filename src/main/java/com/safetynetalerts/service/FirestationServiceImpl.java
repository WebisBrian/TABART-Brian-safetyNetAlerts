package com.safetynetalerts.service;

import com.safetynetalerts.dto.CoveredPersonDto;
import com.safetynetalerts.dto.FirestationCoverageDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service métier pour l'endpoint /firestation.
 * Elle utilise SafetyNetDataRepository pour récupérer les données brutes
 * puis applique la logique métier (filtrage par caserne, calcul adultes/enfants, etc.).
 */
@Service
public class FirestationServiceImpl implements FirestationService {

    private final SafetyNetDataRepository dataRepository;

    public FirestationServiceImpl(SafetyNetDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public FirestationCoverageDto getCoverageByStation(int stationNumber) {

        List<Firestation> firestations = dataRepository.getAllFirestations();

        List<String> coveredAddresses = firestations.stream()
                .filter(f -> f.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .toList();

        if (coveredAddresses.isEmpty()) {
            return new FirestationCoverageDto(List.of(), 0, 0);
        }

        List<Person> allPersons = dataRepository.getAllPersons();

        List<Person> coveredPersons = allPersons.stream()
                .filter(p -> coveredAddresses.contains(p.getAddress()))
                .toList();

        List<MedicalRecord> allMedicalRecords = dataRepository.getAllMedicalRecords();

        int numberOfChildren = (int) coveredPersons.stream()
                .filter(p -> isChild(p, allMedicalRecords))
                .count();

        int numberOfAdults = coveredPersons.size() - numberOfChildren;

        List<CoveredPersonDto> dtoPersons = coveredPersons.stream()
                .map(p -> new CoveredPersonDto(
                        p.getFirstName(),
                        p.getLastName(),
                        p.getAddress(),
                        p.getPhone()
                ))
                .toList();

        return new FirestationCoverageDto(dtoPersons, numberOfAdults, numberOfChildren);
    }

    /**
     * Détermine si une personne est un enfant (< 18 ans) en fonction des dossiers médicaux.
     */
    private boolean isChild(Person person, List<MedicalRecord> medicalRecords) {
        Optional<MedicalRecord> recordOptional = medicalRecords.stream()
                .filter(mr -> mr.getFirstName().equals(person.getFirstName())
                        && mr.getLastName().equals(person.getLastName()))
                .findFirst();
        if (recordOptional.isEmpty()) {
            return false;
        }
        int age = calculateAge(recordOptional.get().getBirthdate());
        return age < 18;
    }

    /**
     * Calcule l'âge à partir d'une date de naissance au format MM/dd/yyyy (ex : "03/06/1984").
     */
    private int calculateAge(String stringBirthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(stringBirthdate, formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
