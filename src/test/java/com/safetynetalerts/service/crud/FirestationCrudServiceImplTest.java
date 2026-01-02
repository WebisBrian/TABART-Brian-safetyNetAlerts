package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FirestationCrudServiceImplTest {

    @Mock
    private FirestationRepository firestationRepository;

    @InjectMocks
    private FirestationCrudServiceImpl service;

    @Test
    void create_shouldDelegateToRepository() {
        // Arrange
        Firestation input = new Firestation("1509 Culver St", 3);
        when(firestationRepository.findByAddressAndByStation("1509 Culver St", 3)).thenReturn(Optional.empty());
        when(firestationRepository.add(input)).thenReturn(input);

        // Act
        Firestation result = service.create(input);

        // Assert
        assertThat(result).isSameAs(input);
        verify(firestationRepository).findByAddressAndByStation("1509 Culver St", 3);
        verify(firestationRepository).add(input);
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void update_shouldReturnTrueWhenRepositoryUpdates() {
        Firestation updated = new Firestation("1509 Culver St", 2);
        when(firestationRepository.update(updated)).thenReturn(true);

        boolean result = service.update(updated);

        assertThat(result).isTrue();
        verify(firestationRepository).update(updated);
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void update_shouldReturnFalseWhenAddressNotFound() {
        Firestation updated = new Firestation("Unknown", 1);
        when(firestationRepository.update(updated)).thenReturn(false);

        boolean result = service.update(updated);

        assertThat(result).isFalse();
        verify(firestationRepository).update(updated);
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void delete_shouldReturnTrueWhenRepositoryDeletes() {
        when(firestationRepository.deleteByAddress("1509 Culver St")).thenReturn(true);

        boolean result = service.delete("1509 Culver St");

        assertThat(result).isTrue();
        verify(firestationRepository).deleteByAddress("1509 Culver St");
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void delete_shouldReturnFalseWhenAddressNotFound() {
        when(firestationRepository.deleteByAddress("Unknown")).thenReturn(false);

        boolean result = service.delete("Unknown");

        assertThat(result).isFalse();
        verify(firestationRepository).deleteByAddress("Unknown");
        verifyNoMoreInteractions(firestationRepository);
    }
}
