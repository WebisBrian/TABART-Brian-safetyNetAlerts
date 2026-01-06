package com.safetynetalerts.service;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.repository.person.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityEmailServiceImpl implements CommunityEmailService {

    private final PersonRepository personRepository;

    public CommunityEmailServiceImpl(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<String> getEmailsByCity(String city) {

        if (city == null || city.isBlank()) {
            throw new BadRequestException("Le nom de la ville doit être renseigné.");
        }

        return personRepository.findAll().stream()
                .filter(p -> p.getCity() != null && p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .filter(e -> e != null && !e.isBlank())
                .distinct()
                .sorted()
                .toList();
    }
}
