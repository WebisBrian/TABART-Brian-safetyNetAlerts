package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.model.exception.ConflictException;
import com.safetynetalerts.repository.medicalrecord.MedicalRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
        MedicalRecord input = MedicalRecord.create("John", "Boyd", "03/06/1984",
                List.of("aznol:350mg"), List.of("nillacilan"));

        when(medicalRecordRepository.findByName("John", "Boyd")).thenReturn(Optional.empty());
        when(medicalRecordRepository.add(input)).thenReturn(input);

        MedicalRecord result = service.create(input);

        assertThat(result).isSameAs(input);
        verify(medicalRecordRepository).findByName("John", "Boyd");
        verify(medicalRecordRepository, times(1)).add(input);
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void create_shouldThrowConflictWhenRecordExists() {
        MedicalRecord input = MedicalRecord.create("John", "Boyd", "03/06/1984",
                List.of(), List.of());

        when(medicalRecordRepository.findByName("John", "Boyd")).thenReturn(Optional.of(input));

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Un dossier médical a déjà été créé pour cette personne.");

        verify(medicalRecordRepository).findByName("John", "Boyd");
        verify(medicalRecordRepository, never()).add(any());
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void update_shouldReturnTrueWhenRepositoryUpdates() {
        MedicalRecord updated = MedicalRecord.create("John", "Boyd", "03/06/1984",
                List.of("new:10mg"), List.of());

        when(medicalRecordRepository.update(updated)).thenReturn(true);

        boolean result = service.update(updated);

        assertThat(result).isTrue();
        verify(medicalRecordRepository).update(updated);
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void update_shouldReturnFalseWhenRecordNotFound() {
        MedicalRecord updated = MedicalRecord.create("X", "Y", "01/01/2000",
                List.of(), List.of());

        when(medicalRecordRepository.update(updated)).thenReturn(false);

        boolean result = service.update(updated);

        assertThat(result).isFalse();
        verify(medicalRecordRepository).update(updated);
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void delete_shouldReturnTrueWhenRepositoryDeletes() {
        when(medicalRecordRepository.delete("John", "Boyd")).thenReturn(true);

        boolean result = service.delete("John", "Boyd");

        assertThat(result).isTrue();
        verify(medicalRecordRepository).delete("John", "Boyd");
        verifyNoMoreInteractions(medicalRecordRepository);
    }

    @Test
    void delete_shouldReturnFalseWhenRecordNotFound() {
        when(medicalRecordRepository.delete("X", "Y")).thenReturn(false);

        boolean result = service.delete("X", "Y");

        assertThat(result).isFalse();
        verify(medicalRecordRepository).delete("X", "Y");
        verifyNoMoreInteractions(medicalRecordRepository);
    }
}