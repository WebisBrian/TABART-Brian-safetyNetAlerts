package com.safetynetalerts.service;

import com.safetynetalerts.dto.flood.FloodAddressDto;
import com.safetynetalerts.dto.flood.FloodResidentDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import com.safetynetalerts.service.util.AgeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloodServiceImpl implements FloodService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AgeService ageService;

    public FloodServiceImpl(final PersonRepository personRepository, final FirestationRepository firestationRepository, final MedicalRecordRepository medicalRecordRepository, final AgeService ageService) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.ageService = ageService;
    }

    @Override
    public List<FloodAddressDto> getFloodInfoByStations(List<Integer> stations) {

        List<Firestation> firestations = firestationRepository.findAll();
        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        // addresses covered by stations
        List<String> addresses = firestations.stream()
                .filter(fs -> stations.contains(fs.getStation()))
                .map(Firestation::getAddress)
                .distinct()
                .toList();

        return addresses.stream()
                .map(address -> {
                    List<Person> residents = persons.stream()
                            .filter(p -> p.getAddress().equalsIgnoreCase(address))
                            .toList();

                    List<FloodResidentDto> residentDtos = residents.stream()
                            .map(p -> {
                                MedicalRecord record = medicalRecords.stream()
                                        .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                                                && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                                        .findFirst()
                                        .orElse(null);

                                int age = ageService.getAge(p, medicalRecords).orElse(0);

                                return new FloodResidentDto(
                                        p.getFirstName(),
                                        p.getLastName(),
                                        p.getPhone(),
                                        age,
                                        record != null ? record.getMedications() : List.of(),
                                        record != null ? record.getAllergies() : List.of()
                                );
                            })
                            .toList();

                    return new FloodAddressDto(address, residentDtos);
                })
                .toList();
    }
}
