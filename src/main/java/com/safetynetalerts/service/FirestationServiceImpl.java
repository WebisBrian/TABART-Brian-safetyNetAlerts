package com.safetynetalerts.service;

import com.safetynetalerts.dto.response.firestation.CoveredPersonDto;
import com.safetynetalerts.dto.response.firestation.FirestationCoverageDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
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

    public FirestationServiceImpl(final PersonRepository personRepository, final FirestationRepository firestationRepository, final MedicalRecordRepository medicalRecordRepository, final AgeService ageService) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageService = ageService;
    }

    @Override
    public FirestationCoverageDto getCoverageByStation(Integer stationNumber) {

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
