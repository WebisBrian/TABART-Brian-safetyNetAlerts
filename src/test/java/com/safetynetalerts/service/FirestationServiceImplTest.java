package com.safetynetalerts.service;

import com.safetynetalerts.dto.firestation.FirestationCoverageDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.service.util.AgeService;
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
class FirestationServiceImplTest {

    @Mock
    private SafetyNetDataRepository dataRepository;

    @Mock
    private AgeService ageService;

    @InjectMocks
    private FirestationServiceImpl firestationService;

    @BeforeEach
    void setUp() {
        // Initialize data for test
        Firestation station1 = new Firestation("1509 Culver St", 1);
        Firestation station2 = new Firestation("834 Binoc Ave", 2);

        when(dataRepository.getAllFirestations()).thenReturn(List.of(station1, station2));

        Person johnBoyd = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person tenleyBoyd = new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person other = new Person("Other", "Person", "29 15th St", "Culver", "97451", "333-333-3333", "other@email.com");

        lenient().when(dataRepository.getAllPersons())
                .thenReturn(List.of(johnBoyd, tenleyBoyd, other));

        MedicalRecord medicalRecordJohn = new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg",
                "hydrapermazol:100mg"), List.of("nillacilan"));
        MedicalRecord medicalRecordTenley = new MedicalRecord("Tenley", "Boyd", "02/18/2012", List.of(), List.of("peanut"));
        MedicalRecord medicalRecordOther = new MedicalRecord("Other", "Person", "01/01/1999", List.of("aznol:350mg"), List.of());

        lenient().when(dataRepository.getAllMedicalRecords())
                .thenReturn(List.of(medicalRecordJohn, medicalRecordTenley, medicalRecordOther));

        lenient().when(ageService.isChild(any(Person.class), anyList())).thenReturn(false);
        lenient().when(ageService.isChild(argThat(p -> p.getFirstName().equals("Tenley")), anyList())).thenReturn(true);
    }

    @Test
    void getCoverageByStation_shouldReturnCorrectPersonsAndCountsForGivenStation() {
        // ACT
        FirestationCoverageDto coverage = firestationService.getCoverageByStation(1);

        // ASSERT
        assertThat(coverage.getPersons()).hasSize(2);
        assertThat(coverage.getNumberOfAdults()).isEqualTo(1);
        assertThat(coverage.getNumberOfChildren()).isEqualTo(1);
        assertThat(coverage.getPersons().get(0).getFirstName()).isEqualTo("John");
        assertThat(coverage.getPersons().get(1).getFirstName()).isEqualTo("Tenley");
    }

    @Test
    void getCoverageByStation_shouldReturnEmptyResultForUnknownStation() {
        // ACT + ASSERT
        FirestationCoverageDto coverage = firestationService.getCoverageByStation(3);

        assertThat(coverage.getPersons()).isEmpty();
        assertThat(coverage.getNumberOfAdults()).isZero();
        assertThat(coverage.getNumberOfChildren()).isZero();
    }

}