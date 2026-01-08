package com.safetynetalerts.service;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

        List<String> coveredAddresses = findCoveredAddressesByStation(stationNumber);
        if (coveredAddresses.isEmpty()) {
            return List.of();
        }

        Set<String> coveredAddressesSet = new HashSet<>(coveredAddresses);

        return findPhonesByCoveredAddresses(coveredAddressesSet);
    }

    private List<String> findCoveredAddressesByStation(Integer stationNumber) {
        return firestationRepository.findAll().stream()
                .filter(fs -> Objects.equals(fs.getStation(), stationNumber))
                .map(Firestation::getAddress)
                .distinct()
                .toList();
    }

    private List<String> findPhonesByCoveredAddresses(Set<String> coveredAddresses) {
        return personRepository.findAll().stream()
                .filter(p -> coveredAddresses.contains(p.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .toList();
    }
}
