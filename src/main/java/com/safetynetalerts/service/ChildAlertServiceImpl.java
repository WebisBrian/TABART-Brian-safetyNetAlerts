package com.safetynetalerts.service;

import com.safetynetalerts.dto.childalert.ChildAlertChildDto;
import com.safetynetalerts.dto.childalert.ChildAlertHouseholdMemberDto;
import com.safetynetalerts.dto.childalert.ChildAlertResponseDto;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service métier pour l'endpoint /childAlert.
 * Utilise SafetyNetDataRepository pour récupérer les données brutes, puis
 * construit la réponse avec la liste des enfants et des autres membres du foyer.
 */
@Service
public class ChildAlertServiceImpl implements ChildAlertService {

    private final SafetyNetDataRepository dataRepository;

    public ChildAlertServiceImpl(SafetyNetDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public ChildAlertResponseDto getChildrenByAddress(String address) {

        List<Person> allPersons = dataRepository.getAllPersons();
        List<MedicalRecord> allMedicalRecords = dataRepository.getAllMedicalRecords();

        List<Person> personsAtAddress = allPersons.stream()
                .filter(p -> p.getAddress().equals(address))
                .toList();

        if (personsAtAddress.isEmpty()) {
            return new ChildAlertResponseDto(List.of(), List.of());
        }

        List<ChildAlertChildDto> children = new ArrayList<>();
        List<ChildAlertHouseholdMemberDto> otherHouseholdMembers = new ArrayList<>();

        for (Person person : personsAtAddress) {
            int age = getAgeForPerson(person, allMedicalRecords);

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

    /**
     * Calcule l'âge d'une personne en utilisant son dossier médical.
     * Si aucun dossier trouvé, la personne est considérée comme adulte (âge 0 par défaut).
     */
    private int getAgeForPerson(Person person, List<MedicalRecord> medicalRecords) {
        Optional<MedicalRecord> recordOpt = medicalRecords.stream()
                .filter(mr -> mr.getFirstName().equals(person.getFirstName())
                        && mr.getLastName().equals(person.getLastName()))
                .findFirst();

        if (recordOpt.isEmpty()) {
            // Si aucune date de naissance, choix simple : considérer comme adulte
            return 0;
        }

        String birthdate = recordOpt.get().getBirthdate();
        return calculateAge(birthdate);
    }

    /**
     * Calcule l'âge à partir d'une date de naissance au format MM/dd/yyyy (ex : "03/06/1984").
     */
    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}

