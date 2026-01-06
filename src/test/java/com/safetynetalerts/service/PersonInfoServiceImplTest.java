package com.safetynetalerts.service;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
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
class PersonInfoServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AgeService ageService;

    @InjectMocks
    private PersonInfoServiceImpl service;

    @BeforeEach
    void setUp() {

        when(personRepository.findAll()).thenReturn(List.of(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841", "john@email.com"),
                new Person("John", "Boyd", "Other Address", "Culver", "97451", "842", "john2@email.com"),
                new Person("Jane", "Doe", "X", "Culver", "97451", "843", "jane@email.com")
        ));

        when(medicalRecordRepository.findAll()).thenReturn(List.of(
                new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg"), List.of("nillacilan")),
                new MedicalRecord("Jane", "Doe", "01/01/1990", List.of(), List.of())
        ));
    }

    @Test
    void getPersonInfo_shouldReturnAllMatchingPersons() {

        when(ageService.getAge(argThat(p -> p != null && "John".equals(p.getFirstName()) && "Boyd".equals(p.getLastName())), anyList()))
                .thenReturn(OptionalInt.of(41));

        // Act + Assert
        var res = service.getPersonInfoByLastName("Boyd");
        assertThat(res.persons()).hasSize(2);
        assertThat(res.persons().getFirst().age()).isEqualTo(41);
        assertThat(res.persons().getFirst().medications()).contains("aznol:350mg");
    }

    @Test
    void getPersonInfo_shouldReturnEmptyWhenNoMatch() {

        var res = service.getPersonInfoByLastName("X");
        assertThat(res.persons()).isEmpty();
    }

}