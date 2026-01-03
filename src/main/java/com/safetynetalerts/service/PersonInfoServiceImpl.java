package com.safetynetalerts.service;

import com.safetynetalerts.dto.personinfo.PersonInfoDto;
import com.safetynetalerts.dto.personinfo.PersonInfoResponseDto;
import com.safetynetalerts.model.MedicalRecord;
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

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        List<PersonInfoDto> persons = personRepository.findAll().stream()
                .filter(p -> p.getFirstName() != null && p.getLastName() != null)
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .map(p -> {
                    Optional<MedicalRecord> recordOpt = medicalRecords.stream()
                            .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                                    && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                            .findFirst();

                    int age = ageService.getAge(p, medicalRecords).orElse(0);

                    return new PersonInfoDto(
                            p.getFirstName(),
                            p.getLastName(),
                            p.getAddress(),
                            age,
                            p.getEmail(),
                            recordOpt.map(MedicalRecord::getMedications).orElse(List.of()),
                            recordOpt.map(MedicalRecord::getAllergies).orElse(List.of())
                    );
                })
                .toList();

        return new PersonInfoResponseDto(persons);
    }
}
