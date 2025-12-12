package com.safetynetalerts.service;

import com.safetynetalerts.dto.childalert.ChildAlertResponseDto;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChildAlertServiceImplTest {

    @Mock
    private SafetyNetDataRepository dataRepository;

    @InjectMocks
    private ChildAlertServiceImpl childAlertService;

    @BeforeEach
    void setUp() {
        // Initialize data for test
        Person johnBoyd = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person tenleyBoyd = new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person other = new Person("Other", "Person", "29 15th St", "Culver", "97451", "333-333-3333", "other@email.com");

        when(dataRepository.getAllPersons())
                .thenReturn(List.of(johnBoyd, tenleyBoyd, other));

        MedicalRecord medicalRecordJohn = new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg",
                "hydrapermazol:100mg"), List.of("nillacilan"));
        MedicalRecord medicalRecordTenley = new MedicalRecord("Tenley", "Boyd", "02/18/2012", List.of(), List.of("peanut"));
        MedicalRecord medicalRecordOther = new MedicalRecord("Other", "Person", "01/01/1999", List.of("aznol:350mg"), List.of());

        when(dataRepository.getAllMedicalRecords())
                .thenReturn(List.of(medicalRecordJohn, medicalRecordTenley, medicalRecordOther));
    }

    @Test
    void getChildrenByAddress_shouldReturnChildrenAndHouseholdMembersForAddressWithChildren() {
        // ACT
        ChildAlertResponseDto response = childAlertService.getChildrenByAddress("1509 Culver St");

        // ASSERT
        assertNotNull(response);
        assertThat(response.getChildren()).hasSize(1);
        assertThat(response.getOtherHouseholdMembers()).hasSize(1);
        assertThat(response.getChildren().get(0).getAge()).isLessThan(18);
        assertThat(response.getOtherHouseholdMembers().get(0).getAge()).isGreaterThanOrEqualTo(18);
        assertThat(response.getChildren().get(0).getFirstName()).isEqualTo("Tenley");
        assertThat(response.getChildren().get(0).getLastName()).isEqualTo("Boyd");
        assertThat(response.getOtherHouseholdMembers().get(0).getFirstName()).isEqualTo("John");
        assertThat(response.getOtherHouseholdMembers().get(0).getLastName()).isEqualTo("Boyd");
        verify(dataRepository, times(1)).getAllPersons();
    }

    @Test
    void getChildrenByAddress_shouldReturnEmptyListsForAddressWithoutChildren() {
        // ACT
        ChildAlertResponseDto response = childAlertService.getChildrenByAddress("29 15th St");

        // ASSERT
        assertNotNull(response);
        assertTrue(response.getChildren().isEmpty());
        assertTrue(response.getOtherHouseholdMembers().isEmpty());
        verify(dataRepository, times(1)).getAllPersons();
    }
}