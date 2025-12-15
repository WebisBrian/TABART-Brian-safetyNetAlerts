package com.safetynetalerts.service;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final SafetyNetDataRepository dataRepository;

    public PhoneAlertServiceImpl(final SafetyNetDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<String> getPhonesByStation(int stationNumber) {

        List<String> coveredAddresses = dataRepository.getAllFirestations().stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .distinct()
                .toList();

        if (coveredAddresses.isEmpty()) {
            return List.of();
        }

        return dataRepository.getAllPersons().stream()
                .filter(p -> coveredAddresses.contains(p.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .toList();
    }
}
