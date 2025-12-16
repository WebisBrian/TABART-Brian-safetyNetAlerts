package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.SafetyNetDataRepository;
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
    private SafetyNetDataRepository repo;

    @InjectMocks
    private MedicalRecordCrudServiceImpl service;

    @Test
    void create_shouldDelegateToRepository() {
        // Arrange
        MedicalRecord input = new MedicalRecord("John", "Boyd", "03/06/1984",
                List.of("aznol:350mg"), List.of("nillacilan"));

        when(repo.addMedicalRecord(input)).thenReturn(input);

        // Act
        MedicalRecord result = service.create(input);

        // Assert
        assertThat(result).isSameAs(input);
        verify(repo).addMedicalRecord(input);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_shouldReturnTrueWhenRepositoryUpdates() {
        // Arrange
        MedicalRecord updated = new MedicalRecord("John", "Boyd", "03/06/1984",
                List.of("new:10mg"), List.of());

        when(repo.updateMedicalRecord(updated)).thenReturn(true);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isTrue();
        verify(repo).updateMedicalRecord(updated);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_shouldReturnFalseWhenRecordNotFound() {
        // Arrange
        MedicalRecord updated = new MedicalRecord("X", "Y", "01/01/2000",
                List.of(), List.of());

        when(repo.updateMedicalRecord(updated)).thenReturn(false);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isFalse();
        verify(repo).updateMedicalRecord(updated);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_shouldReturnTrueWhenRepositoryDeletes() {
        // Arrange
        when(repo.deleteMedicalRecord("John", "Boyd")).thenReturn(true);

        // Act
        boolean result = service.delete("John", "Boyd");

        // Assert
        assertThat(result).isTrue();
        verify(repo).deleteMedicalRecord("John", "Boyd");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_shouldReturnFalseWhenRecordNotFound() {
        // Arrange
        when(repo.deleteMedicalRecord("X", "Y")).thenReturn(false);

        // Act
        boolean result = service.delete("X", "Y");

        // Assert
        assertThat(result).isFalse();
        verify(repo).deleteMedicalRecord("X", "Y");
        verifyNoMoreInteractions(repo);
    }
}