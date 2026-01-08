package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.model.exception.ConflictException;
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
        Person input = Person.create("John", "Boyd", "1509 Culver St", "Culver", "97451", "841", "john@email.com");

        when(personRepository.findByName("John", "Boyd")).thenReturn(Optional.empty());
        when(personRepository.add(input)).thenReturn(input);

        Person result = service.create(input);

        assertThat(result).isSameAs(input);
        verify(personRepository).findByName("John", "Boyd");
        verify(personRepository, times(1)).add(input);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void create_shouldThrowConflictWhenPersonExists() {
        Person input = Person.create("John", "Boyd", "1509 Culver St", "Culver", "97451", "841", "john@email.com");
        when(personRepository.findByName("John", "Boyd")).thenReturn(Optional.of(new Person()));

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Une personne avec le même nom et prénom a déjà été ajoutée.");

        verify(personRepository).findByName("John", "Boyd");
        verify(personRepository, never()).add(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void create_shouldThrowBadRequestWhenNameMissing() {
        Person input = Person.create(null, "", "1509 Culver St", "Culver", "97451", "841", "a@b.com");
        when(personRepository.findByName(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Le prénom et le nom doivent être renseignés.");

        verify(personRepository).findByName(null, "");
        verify(personRepository, never()).add(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void create_shouldThrowBadRequestWhenAddressMissing() {
        Person input = Person.create("A", "B", null, "Culver", "97451", "841", "a@b.com");
        when(personRepository.findByName(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("L'adresse doit être renseignée.");

        verify(personRepository).findByName("A", "B");
        verify(personRepository, never()).add(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void create_shouldThrowBadRequestWhenCityOrZipMissing() {
        Person input = Person.create("A", "B", "addr", null, "", "841", "a@b.com");
        when(personRepository.findByName(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("La ville et le code postal doivent être renseignés.");

        verify(personRepository).findByName("A", "B");
        verify(personRepository, never()).add(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void create_shouldThrowBadRequestWhenPhoneMissing() {
        Person input = Person.create("A", "B", "addr", "Culver", "97451", "", "a@b.com");
        when(personRepository.findByName(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Le numéro de téléphone doit être renseigné.");

        verify(personRepository).findByName("A", "B");
        verify(personRepository, never()).add(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void create_shouldThrowBadRequestWhenEmailMissing() {
        Person input = Person.create("A", "B", "addr", "Culver", "97451", "841", " ");
        when(personRepository.findByName(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("L'email doit être renseigné.");

        verify(personRepository).findByName("A", "B");
        verify(personRepository, never()).add(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void update_shouldReturnTrueWhenRepositoryUpdates() {
        Person updated = Person.create("John", "Boyd", "NEW", "Culver", "97451", "999", "new@email.com");
        when(personRepository.update(updated)).thenReturn(true);

        boolean result = service.update(updated);

        assertThat(result).isTrue();
        verify(personRepository).update(updated);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void update_shouldThrowBadRequestWhenAddressMissing() {
        Person updated = Person.create("A", "B", "", "Culver", "97451", "999", "a@b.com");

        assertThatThrownBy(() -> service.update(updated))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("L'adresse doit être renseignée.");

        verify(personRepository, never()).update(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void update_shouldThrowBadRequestWhenCityOrZipMissing() {
        Person updated = Person.create("A", "B", "addr", null, "", "999", "a@b.com");

        assertThatThrownBy(() -> service.update(updated))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("La ville et le code postal doivent être renseignés.");

        verify(personRepository, never()).update(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void update_shouldThrowBadRequestWhenPhoneMissing() {
        Person updated = Person.create("A", "B", "addr", "Culver", "97451", " ", "a@b.com");

        assertThatThrownBy(() -> service.update(updated))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Le numéro de téléphone doit être renseigné.");

        verify(personRepository, never()).update(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void update_shouldThrowBadRequestWhenEmailMissing() {
        Person updated = Person.create("A", "B", "addr", "Culver", "97451", "999", null);

        assertThatThrownBy(() -> service.update(updated))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("L'email doit être renseigné.");

        verify(personRepository, never()).update(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void delete_shouldReturnTrueWhenRepositoryDeletes() {
        when(personRepository.delete("John", "Boyd")).thenReturn(true);

        boolean result = service.delete("John", "Boyd");

        assertThat(result).isTrue();
        verify(personRepository).delete("John", "Boyd");
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void delete_shouldReturnFalseWhenPersonNotFound() {
        when(personRepository.delete("X", "Y")).thenReturn(false);

        boolean result = service.delete("X", "Y");

        assertThat(result).isFalse();
        verify(personRepository).delete("X", "Y");
        verifyNoMoreInteractions(personRepository);
    }
}
