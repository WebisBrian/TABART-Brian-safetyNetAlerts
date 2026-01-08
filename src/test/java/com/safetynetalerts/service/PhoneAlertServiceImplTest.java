package com.safetynetalerts.service;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneAlertServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @InjectMocks
    private PhoneAlertServiceImpl phoneAlertService;

    @BeforeEach
    void setUp() {

        Firestation station1 = new Firestation("1509 Culver St", 1);
        Firestation station2 = new Firestation("29 15th St", 2);

        when(firestationRepository.findAll())
                .thenReturn(List.of(station1, station2));
    }

    @Test
    void getPhonesByStation_shouldReturnDistinctPhonesForKnownStation() {

        Person john = Person.create("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "john@email.com");
        Person tenley = Person.create("Tenley", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "tenley@email.com"); // même téléphone
        Person other = Person.create("Other", "Person", "29 15th St", "Culver",
                "97451", "333-333-3333", "other@email.com");

        when(personRepository.findAll())
                .thenReturn(List.of(john, tenley, other));

        // Act
        List<String> phones = phoneAlertService.getPhonesByStation(1);

        // Assert
        assertThat(phones)
                .hasSize(1)
                .containsExactly("841-874-6512");
    }

    @Test
    void getPhonesByStation_shouldReturnEmptyListForUnknownStation() {
        // Act
        List<String> phones = phoneAlertService.getPhonesByStation(3);

        // Assert
        assertThat(phones).isEmpty();
    }

}