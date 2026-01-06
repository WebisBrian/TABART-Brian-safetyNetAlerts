package com.safetynetalerts.service;

import com.safetynetalerts.dto.response.childalert.ChildAlertChildDto;
import com.safetynetalerts.dto.response.childalert.ChildAlertHouseholdMemberDto;
import com.safetynetalerts.dto.response.childalert.ChildAlertResponseDto;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import com.safetynetalerts.service.util.AgeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * Implémentation du service métier pour l'endpoint /childAlert.
 * Utilise SafetyNetDataRepository pour récupérer les données brutes, puis
 * construit la réponse avec la liste des enfants et des autres membres du foyer.
 */
@Service
public class ChildAlertServiceImpl implements ChildAlertService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AgeService ageService;

    public ChildAlertServiceImpl(final PersonRepository personRepository, final MedicalRecordRepository medicalRecordRepository, final AgeService ageService) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageService = ageService;
    }

    @Override
    public ChildAlertResponseDto getChildrenByAddress(String address) {

        if (address == null || address.isBlank()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }

        List<Person> allPersons = personRepository.findAll();
        List<MedicalRecord> allMedicalRecords = medicalRecordRepository.findAll();

        List<Person> personsAtAddress = allPersons.stream()
                .filter(p -> p.getAddress().equals(address))
                .toList();

        if (personsAtAddress.isEmpty()) {
            return new ChildAlertResponseDto(List.of(), List.of());
        }

        List<ChildAlertChildDto> children = new ArrayList<>();
        List<ChildAlertHouseholdMemberDto> otherHouseholdMembers = new ArrayList<>();

        for (Person person : personsAtAddress) {
            OptionalInt ageOpt = ageService.getAge(person, allMedicalRecords);

            // Stratégie : Si pas de dossier médical, on considère comme adulte
            int age = ageOpt.orElse(999);

            if (age < 18) {
                children.add(new ChildAlertChildDto(
                        person.getFirstName(),
                        person.getLastName(),
                        age
                ));
            } else {
                otherHouseholdMembers.add(new ChildAlertHouseholdMemberDto(
                        person.getFirstName(),
                        person.getLastName(),
                        age,
                        person.getPhone()
                ));
            }
        }

        if (children.isEmpty()) {
            return new ChildAlertResponseDto(List.of(), List.of());
        }

        return new ChildAlertResponseDto(children, otherHouseholdMembers);
    }
}

