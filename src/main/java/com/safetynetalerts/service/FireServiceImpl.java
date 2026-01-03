package com.safetynetalerts.service;

import com.safetynetalerts.dto.fire.FireResidentDto;
import com.safetynetalerts.dto.fire.FireResponseDto;
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
import java.util.Optional;

@Service
public class FireServiceImpl implements FireService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AgeService ageService;


    public FireServiceImpl(final PersonRepository personRepository, final FirestationRepository firestationRepository, final MedicalRecordRepository medicalRecordRepository , final AgeService ageService) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageService = ageService;
    }

    @Override
    public FireResponseDto getFireInfoByAddress(String address) {

        if (address == null || address.isBlank()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }

        int stationNumber = firestationRepository.findAll().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .mapToInt(Firestation::getStation)
                .findFirst()
                .orElse(0);

        List<Person> residentsAtAddress = personRepository.findAll().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();


        if (residentsAtAddress.isEmpty() || stationNumber == 0) {
            return new FireResponseDto(0, List.of());
        }

        List<MedicalRecord> allMedicalRecords = medicalRecordRepository.findAll();

        List<FireResidentDto> residents = residentsAtAddress.stream()
                .map(p -> {
                    Optional<MedicalRecord> recordOpt = allMedicalRecords.stream()
                            .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                                    && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                            .findFirst();

                    int age = ageService.getAge(p, allMedicalRecords).orElse(999);

                    return new FireResidentDto(
                            p.getFirstName(),
                            p.getLastName(),
                            p.getPhone(),
                            age,
                            recordOpt.map(MedicalRecord::getMedications).orElse(List.of()),
                            recordOpt.map(MedicalRecord::getAllergies).orElse(List.of())
                    );
                })
                .toList();

        return new FireResponseDto(stationNumber, residents);
    }
}
