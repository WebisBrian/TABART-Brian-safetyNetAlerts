package com.safetynetalerts.service;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    public PhoneAlertServiceImpl(final PersonRepository personRepository, final FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
    }

    @Override
    public List<String> getPhonesByStation(Integer stationNumber) {

        if (stationNumber == null || stationNumber < 0) {
            throw new BadRequestException("Le numéro de caserne doit être un entier positif.");
        }

        List<String> coveredAddresses = firestationRepository.findAll().stream()
                .filter(fs -> Objects.equals(fs.getStation(), stationNumber))
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
