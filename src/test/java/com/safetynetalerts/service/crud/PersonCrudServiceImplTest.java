package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.person.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonCrudServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonCrudServiceImpl service;

    @Test
    void create_shouldDelegateToRepository() {
        // Arrange
        Person input = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841", "john@email.com");

        when(personRepository.findByName("John", "Boyd")).thenReturn(Optional.empty());
        when(personRepository.add(input)).thenReturn(input);

        // Act
        Person result = service.create(input);

        // Assert
        assertThat(result).isSameAs(input);
        verify(personRepository).findByName("John", "Boyd");
        verify(personRepository, times(1)).add(input);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void update_shouldReturnTrueWhenRepositoryUpdates() {
        // Arrange
        Person updated = new Person("John", "Boyd", "NEW", "Culver", "97451", "999", "new@email.com");
        when(personRepository.update(updated)).thenReturn(true);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isTrue();
        verify(personRepository).update(updated);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void update_shouldReturnFalseWhenPersonNotFound() {
        // Arrange
        Person updated = new Person("X", "Y", "NEW", "Culver", "97451", "999", "new@email.com");
        when(personRepository.update(updated)).thenReturn(false);

        // Act
        boolean result = service.update(updated);

        // Assert
        assertThat(result).isFalse();
        verify(personRepository).update(updated);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void delete_shouldReturnTrueWhenRepositoryDeletes() {
        // Arrange
        when(personRepository.delete("John", "Boyd")).thenReturn(true);

        // Act
        boolean result = service.delete("John", "Boyd");

        // Assert
        assertThat(result).isTrue();
        verify(personRepository).delete("John", "Boyd");
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void delete_shouldReturnFalseWhenPersonNotFound() {
        // Arrange
        when(personRepository.delete("X", "Y")).thenReturn(false);

        // Act
        boolean result = service.delete("X", "Y");

        // Assert
        assertThat(result).isFalse();
        verify(personRepository).delete("X", "Y");
        verifyNoMoreInteractions(personRepository);
    }
}
