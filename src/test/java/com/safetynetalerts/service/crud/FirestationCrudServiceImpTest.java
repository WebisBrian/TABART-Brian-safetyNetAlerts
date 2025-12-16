package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;

import com.safetynetalerts.repository.SafetyNetDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FirestationCrudServiceImplTest {

    @Mock
    private SafetyNetDataRepository repo;

    @InjectMocks
    private FirestationCrudServiceImpl service;

    @Test
    void create_shouldDelegateToRepository() {
        // Arrange
        Firestation input = new Firestation("1509 Culver St", 3);
        when(repo.addFirestation(input)).thenReturn(input);

        // Act
        Firestation result = service.create(input);

        // Assert
        assertThat(result).isSameAs(input);
        verify(repo).addFirestation(input);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_shouldReturnTrueWhenRepositoryUpdates() {
        // Arrange
        Firestation updated = new Firestation("1509 Culver St", 2);
        when(repo.updateFirestation(updated)).thenReturn(true);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isTrue();
        verify(repo).updateFirestation(updated);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_shouldReturnFalseWhenAddressNotFound() {
        // Arrange
        Firestation updated = new Firestation("Unknown", 1);
        when(repo.updateFirestation(updated)).thenReturn(false);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isFalse();
        verify(repo).updateFirestation(updated);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_shouldReturnTrueWhenRepositoryDeletes() {
        // Arrange
        when(repo.deleteFirestation("1509 Culver St")).thenReturn(true);

        // Act
        boolean result = service.delete("1509 Culver St");

        // Assert
        assertThat(result).isTrue();
        verify(repo).deleteFirestation("1509 Culver St");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_shouldReturnFalseWhenAddressNotFound() {
        // Arrange
        when(repo.deleteFirestation("Unknown")).thenReturn(false);

        // Act
        boolean result = service.delete("Unknown");

        // Assert
        assertThat(result).isFalse();
        verify(repo).deleteFirestation("Unknown");
        verifyNoMoreInteractions(repo);
    }
}