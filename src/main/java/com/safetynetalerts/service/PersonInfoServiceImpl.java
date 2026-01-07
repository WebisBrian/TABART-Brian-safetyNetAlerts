package com.safetynetalerts.service;

import com.safetynetalerts.dto.response.personinfo.PersonInfoDto;
import com.safetynetalerts.dto.response.personinfo.PersonInfoResponseDto;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import com.safetynetalerts.service.util.AgeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AgeService ageService;

    public PersonInfoServiceImpl(final PersonRepository personRepository, final MedicalRecordRepository medicalRecordRepository, final AgeService ageService) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageService = ageService;
    }

    @Override
    public PersonInfoResponseDto getPersonInfoByLastName(String lastName) {

        if (lastName == null || lastName.isBlank()) {
            throw new BadRequestException("Le nom de famille doit être renseigné.");
        }

        List<PersonInfoDto> persons = findPersonInfoByLastName(lastName);

        return new PersonInfoResponseDto(persons);
    }

    private List<PersonInfoDto> findPersonInfoByLastName(String lastName) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        return personRepository.findAll().stream()
                .filter(p -> p.getFirstName() != null && p.getLastName() != null)
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .map(p -> {
                    Optional<MedicalRecord> recordOpt = medicalRecords.stream()
                            .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                                    && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                            .findFirst();

                    int age = ageService.getAge(p, medicalRecords).orElse(0);

                    return personInfoToDto(p, recordOpt, age);

                })
                .toList();
    }

    private PersonInfoDto personInfoToDto(Person person, Optional<MedicalRecord> recordOpt, int age) {
        return new PersonInfoDto(
                person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                age,
                person.getEmail(),
                recordOpt.map(MedicalRecord::getMedications).orElse(List.of()),
                recordOpt.map(MedicalRecord::getAllergies).orElse(List.of()));
    }
}