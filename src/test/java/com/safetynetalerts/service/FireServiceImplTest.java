package com.safetynetalerts.service;

import com.safetynetalerts.dto.fire.FireResidentDto;
import com.safetynetalerts.dto.fire.FireResponseDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.service.util.AgeService;
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
class FireServiceImplTest {

    @Mock
    private SafetyNetDataRepository dataRepository;

    @Mock
    AgeService ageService;

    @InjectMocks
    private FireServiceImpl fireService;

    @Test
    void getFireInfoByAddress_shouldReturnStationAndResidentsForKnownAddress() {
        // Arrange
        when(dataRepository.getAllFirestations()).thenReturn(List.of(
                new Firestation("1509 Culver St", 3),
                new Firestation("29 15th St", 2)
        ));

        when(dataRepository.getAllPersons()).thenReturn(List.of(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john@email.com"),
                new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "tenley@email.com"),
                new Person("Other", "Person", "29 15th St", "Culver", "97451", "333-333-3333", "other@email.com")
        ));

        when(dataRepository.getAllMedicalRecords()).thenReturn(List.of(
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

        when(ageService.getAge(argThat(p -> p != null && "John".equals(p.getFirstName())), anyList()))
                .thenReturn(OptionalInt.of(41));
        when(ageService.getAge(argThat(p -> p != null && "Tenley".equals(p.getFirstName())), anyList()))
                .thenReturn(OptionalInt.of(12));

        // Act
        FireResponseDto response = fireService.getFireInfoByAddress("1509 Culver St");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStationNumber()).isEqualTo(3);
        assertThat(response.getResidents()).hasSize(2);

        FireResidentDto john = response.getResidents().stream()
                .filter(r -> "John".equals(r.getFirstName()))
                .findFirst()
                .orElseThrow();

        assertThat(john.getPhone()).isEqualTo("841-874-6512");
        assertThat(john.getAge()).isEqualTo(41);
        assertThat(john.getMedications()).contains("aznol:350mg");
        assertThat(john.getAllergies()).contains("nillacilan");
    }

    @Test
    void getFireInfoByAddress_shouldReturnEmptyResultForUnknownAddress() {
        // Act
        FireResponseDto response = fireService.getFireInfoByAddress("UNKNOWN");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getResidents()).isEmpty();
        assertThat(response.getStationNumber()).isZero();
    }
}