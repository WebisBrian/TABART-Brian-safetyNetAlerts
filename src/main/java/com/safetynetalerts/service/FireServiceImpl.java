package com.safetynetalerts.service;

import com.safetynetalerts.dto.fire.FireResidentDto;
import com.safetynetalerts.dto.fire.FireResponseDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.service.util.AgeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class FireServiceImpl implements FireService {

    private final SafetyNetDataRepository dataRepository;
    private final AgeService ageService;


    public FireServiceImpl(SafetyNetDataRepository dataRepository, AgeService ageService) {
        this.dataRepository = dataRepository;
        this.ageService = ageService;
    }

    @Override
    public FireResponseDto getFireInfoByAddress(String address) {

        int stationNumber = dataRepository.getAllFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .mapToInt(Firestation::getStation)
                .findFirst()
                .orElse(0);

        List<Person> residentsAtAddress = dataRepository.getAllPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();


        if (residentsAtAddress.isEmpty() || stationNumber == 0) {
            return new FireResponseDto(0, List.of());
        }

        List<MedicalRecord> allMedicalRecords = dataRepository.getAllMedicalRecords();

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
