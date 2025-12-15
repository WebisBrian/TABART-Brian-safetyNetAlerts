package com.safetynetalerts.service;

import com.safetynetalerts.dto.firestation.CoveredPersonDto;
import com.safetynetalerts.dto.firestation.FirestationCoverageDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.service.util.AgeService;
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
    private final AgeService ageService;

    public FirestationServiceImpl(SafetyNetDataRepository dataRepository, AgeService ageService) {
        this.dataRepository = dataRepository;
        this.ageService = ageService;
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
