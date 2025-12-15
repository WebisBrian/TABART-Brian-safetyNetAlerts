package com.safetynetalerts.service;

import com.safetynetalerts.dto.communityemail.CommunityEmailResponseDto;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityEmailServiceImpl implements CommunityEmailService {

    private final SafetyNetDataRepository dataRepository;

    public CommunityEmailServiceImpl(SafetyNetDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public CommunityEmailResponseDto getEmailsByCity(String city) {
        if (city == null || city.isBlank()) {
            return new CommunityEmailResponseDto(List.of());
        }

        List<String> emails = dataRepository.getAllPersons().stream()
                .filter(p -> p.getCity() != null && p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .filter(e -> e != null && !e.isBlank())
                .distinct()
                .sorted()
                .toList();

        return new CommunityEmailResponseDto(emails);
    }
}
