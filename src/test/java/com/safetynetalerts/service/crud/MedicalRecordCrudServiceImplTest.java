package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordCrudServiceImplTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordCrudServiceImpl service;

    @Test
    void create_shouldDelegateToRepository() {
        // Arrange
        MedicalRecord input = new MedicalRecord("John", "Boyd", "03/06/1984",
                List.of("aznol:350mg"), List.of("nillacilan"));

        when(medicalRecordRepository.add(input)).thenReturn(input);

        // Act
        MedicalRecord result = service.create(input);

        // Assert
        assertThat(result).isSameAs(input);
        verify(medicalRecordRepository).add(input);
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void update_shouldReturnTrueWhenRepositoryUpdates() {
        // Arrange
        MedicalRecord updated = new MedicalRecord("John", "Boyd", "03/06/1984",
                List.of("new:10mg"), List.of());

        when(medicalRecordRepository.update(updated)).thenReturn(true);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isTrue();
        verify(medicalRecordRepository).update(updated);
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void update_shouldReturnFalseWhenRecordNotFound() {
        // Arrange
        MedicalRecord updated = new MedicalRecord("X", "Y", "01/01/2000",
                List.of(), List.of());

        when(medicalRecordRepository.update(updated)).thenReturn(false);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isFalse();
        verify(medicalRecordRepository).update(updated);
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void delete_shouldReturnTrueWhenRepositoryDeletes() {
        // Arrange
        when(medicalRecordRepository.delete("John", "Boyd")).thenReturn(true);

        // Act
        boolean result = service.delete("John", "Boyd");

        // Assert
        assertThat(result).isTrue();
        verify(medicalRecordRepository).delete("John", "Boyd");
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void delete_shouldReturnFalseWhenRecordNotFound() {
        // Arrange
        when(medicalRecordRepository.delete("X", "Y")).thenReturn(false);

        // Act
        boolean result = service.delete("X", "Y");

        // Assert
        assertThat(result).isFalse();
        verify(medicalRecordRepository).delete("X", "Y");
        verifyNoMoreInteractions(medicalRecordRepository);
    }
}