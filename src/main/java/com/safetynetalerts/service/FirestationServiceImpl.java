package com.safetynetalerts.service;

import com.safetynetalerts.dto.firestation.CoveredPersonDto;
import com.safetynetalerts.dto.firestation.FirestationCoverageDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import com.safetynetalerts.service.util.AgeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du service métier pour l'endpoint /firestation.
 * Elle utilise SafetyNetDataRepository pour récupérer les données brutes
 * puis applique la logique métier (filtrage par caserne, calcul adultes/enfants, etc.).
 */
@Service
public class FirestationServiceImpl implements FirestationService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AgeService ageService;

    public FirestationServiceImpl(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository, AgeService ageService) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageService = ageService;
    }

    @Override
    public FirestationCoverageDto getCoverageByStation(int stationNumber) {

        List<Firestation> firestations = firestationRepository.findAll();

        List<String> coveredAddresses = firestations.stream()
                .filter(f -> f.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .toList();

        if (coveredAddresses.isEmpty()) {
            return new FirestationCoverageDto(List.of(), 0, 0);
        }

        List<Person> allPersons = personRepository.findAll();

        List<Person> coveredPersons = allPersons.stream()
                .filter(p -> coveredAddresses.contains(p.getAddress()))
                .toList();

        List<MedicalRecord> allMedicalRecords = medicalRecordRepository.findAll();

        int numberOfChildren = (int) coveredPersons.stream()
                .filter(p -> ageService.isChild(p, allMedicalRecords))
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
}
