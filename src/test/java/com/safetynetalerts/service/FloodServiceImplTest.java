package com.safetynetalerts.service;

import com.safetynetalerts.dto.common.ResidentWithMedicalInfoDto;
import com.safetynetalerts.dto.response.flood.FloodAddressDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;

import com.safetynetalerts.repository.firestation.FirestationRepository;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import com.safetynetalerts.repository.person.PersonRepository;
import com.safetynetalerts.service.util.AgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FloodServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AgeService ageService;

    @InjectMocks
    private FloodServiceImpl floodService;

    @BeforeEach
    void setUp() {

        // Firestations
        when(firestationRepository.findAll()).thenReturn(List.of(
                new Firestation("1509 Culver St", 1),
                new Firestation("29 15th St", 1),
                new Firestation("834 Binoc Ave", 2)
        ));

        // Persons
        when(personRepository.findAll()).thenReturn(List.of(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john@email.com"),
                new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "tenley@email.com"),
                new Person("Other", "Person", "29 15th St", "Culver", "97451", "333-333-3333", "other@email.com"),
                new Person("NotCovered", "Guy", "951 LoneTree Rd", "Culver", "97451", "999-999-9999", "nc@email.com")
        ));

        // Medical records
        when(medicalRecordRepository.findAll()).thenReturn(List.of(
                new MedicalRecord("John", "Boyd", "03/06/1984",
                        List.of("aznol:350mg", "hydrapermazol:100mg"),
                        List.of("nillacilan")),
                new MedicalRecord("Tenley", "Boyd", "02/18/2012",
                        List.of(),
                        List.of("peanut")),
                new MedicalRecord("Other", "Person", "01/01/1999",
                        List.of("aznol:350mg"),
                        List.of())
        ));
    }

    @Test
    void getFloodInfoByStations_shouldReturnAddressesWithResidentsForGivenStations() {

        when(ageService.getAge(argThat(p -> p != null && "John".equals(p.getFirstName())), anyList()))
                .thenReturn(OptionalInt.of(41));
        when(ageService.getAge(argThat(p -> p != null && "Tenley".equals(p.getFirstName())), anyList()))
                .thenReturn(OptionalInt.of(12));
        when(ageService.getAge(argThat(p -> p != null && "Other".equals(p.getFirstName())), anyList()))
                .thenReturn(OptionalInt.of(26));

        // Act
        List<FloodAddressDto> result = floodService.getFloodInfoByStations(List.of(1));

        // Assert
        assertThat(result).hasSize(2);

        FloodAddressDto culver = result.stream()
                .filter(a -> "1509 Culver St".equals(a.address()))
                .findFirst()
                .orElseThrow();

        assertThat(culver.residents()).hasSize(2);

        ResidentWithMedicalInfoDto john = culver.residents().stream()
                .filter(r -> "John".equals(r.firstName()))
                .findFirst()
                .orElseThrow();

        assertThat(john.age()).isEqualTo(41);
        assertThat(john.medications()).contains("aznol:350mg");
        assertThat(john.allergies()).contains("nillacilan");

        FloodAddressDto street29 = result.stream()
                .filter(a -> "29 15th St".equals(a.address()))
                .findFirst()
                .orElseThrow();

        assertThat(street29.residents()).hasSize(1);
        assertThat(street29.residents().get(0).firstName()).isEqualTo("Other");
    }

    @Test
    void getFloodInfoByStations_shouldReturnEmptyListForUnknownStations() {
        // Act
        List<FloodAddressDto> result = floodService.getFloodInfoByStations(List.of(99));

        // Assert
        assertThat(result).isEmpty();
    }
}