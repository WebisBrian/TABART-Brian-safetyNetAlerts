package com.safetynetalerts.service;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    public PhoneAlertServiceImpl(final PersonRepository personRepository, final FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
    }

    @Override
    public List<String> getPhonesByStation(int stationNumber) {

        List<String> coveredAddresses = firestationRepository.findAll().stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .distinct()
                .toList();

        if (coveredAddresses.isEmpty()) {
            return List.of();
        }

        return personRepository.findAll().stream()
                .filter(p -> coveredAddresses.contains(p.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .toList();
    }
}
