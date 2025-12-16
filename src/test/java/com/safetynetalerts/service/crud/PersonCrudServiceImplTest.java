package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonCrudServiceImplTest {

    @Mock
    private SafetyNetDataRepository repo;

    @InjectMocks
    private PersonCrudServiceImpl service;

    @Test
    void create_shouldDelegateToRepository() {
        // Arrange
        Person input = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841", "john@email.com");
        when(repo.addPerson(input)).thenReturn(input);

        // Act
        Person result = service.create(input);

        // Assert
        assertThat(result).isSameAs(input);
        verify(repo, times(1)).addPerson(input);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_shouldReturnTrueWhenRepositoryUpdates() {
        // Arrange
        Person updated = new Person("John", "Boyd", "NEW", "Culver", "97451", "999", "new@email.com");
        when(repo.updatePerson(updated)).thenReturn(true);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isTrue();
        verify(repo).updatePerson(updated);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_shouldReturnFalseWhenPersonNotFound() {
        // Arrange
        Person updated = new Person("X", "Y", "NEW", "Culver", "97451", "999", "new@email.com");
        when(repo.updatePerson(updated)).thenReturn(false);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isFalse();
        verify(repo).updatePerson(updated);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_shouldReturnTrueWhenRepositoryDeletes() {
        // Arrange
        when(repo.deletePerson("John", "Boyd")).thenReturn(true);

        // Act
        boolean result = service.delete("John", "Boyd");

        // Assert
        assertThat(result).isTrue();
        verify(repo).deletePerson("John", "Boyd");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_shouldReturnFalseWhenPersonNotFound() {
        // Arrange
        when(repo.deletePerson("X", "Y")).thenReturn(false);

        // Act
        boolean result = service.delete("X", "Y");

        // Assert
        assertThat(result).isFalse();
        verify(repo).deletePerson("X", "Y");
        verifyNoMoreInteractions(repo);
    }
}