package com.safetynetalerts.service;

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
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonInfoServiceImplTest {

    @Mock
    private SafetyNetDataRepository dataRepository;

    @Mock
    private AgeService ageService;

    @InjectMocks
    private PersonInfoServiceImpl service;

    @BeforeEach
    void setUp() {
        when(dataRepository.getAllPersons()).thenReturn(List.of(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841", "john@email.com"),
                new Person("John", "Boyd", "Other Address", "Culver", "97451", "842", "john2@email.com"),
                new Person("Jane", "Doe", "X", "Culver", "97451", "843", "jane@email.com")
        ));

        when(dataRepository.getAllMedicalRecords()).thenReturn(List.of(
                new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg"), List.of("nillacilan")),
                new MedicalRecord("Jane", "Doe", "01/01/1990", List.of(), List.of())
        ));
    }

    @Test
    void getPersonInfo_shouldReturnAllMatchingPersons() {
        // Arrange
        when(ageService.getAge(argThat(p -> p != null && "John".equals(p.getFirstName()) && "Boyd".equals(p.getLastName())), anyList()))
                .thenReturn(OptionalInt.of(41));

        // Act + Assert
        var res = service.getPersonInfo("John", "Boyd");
        assertThat(res.getPersons()).hasSize(2);
        assertThat(res.getPersons().getFirst().getAge()).isEqualTo(41);
        assertThat(res.getPersons().getFirst().getMedications()).contains("aznol:350mg");
    }

    @Test
    void getPersonInfo_shouldReturnEmptyWhenNoMatch() {
        var res = service.getPersonInfo("X", "Y");
        assertThat(res.getPersons()).isEmpty();
    }

}