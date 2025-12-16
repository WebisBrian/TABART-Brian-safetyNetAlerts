package com.safetynetalerts.service;

import com.safetynetalerts.dto.personinfo.PersonInfoDto;
import com.safetynetalerts.dto.personinfo.PersonInfoResponseDto;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.service.util.AgeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    private final SafetyNetDataRepository dataRepository;
    private final AgeService ageService;

    public PersonInfoServiceImpl(SafetyNetDataRepository dataRepository, AgeService ageService) {
        this.dataRepository = dataRepository;
        this.ageService = ageService;
    }

    @Override
    public PersonInfoResponseDto getPersonInfo(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isBlank() || lastName.isBlank()) {
            return new PersonInfoResponseDto(List.of());
        }

        List<MedicalRecord> medicalRecords = dataRepository.getAllMedicalRecords();

        List<PersonInfoDto> persons = dataRepository.getAllPersons().stream()
                .filter(p -> p.getFirstName() != null && p.getLastName() != null)
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
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
