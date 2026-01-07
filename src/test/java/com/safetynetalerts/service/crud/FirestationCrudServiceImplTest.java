package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.model.exception.ConflictException;
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
        Firestation input = new Firestation("1509 Culver St", 3);
        when(firestationRepository.findByAddressAndByStation("1509 Culver St", 3)).thenReturn(Optional.empty());
        when(firestationRepository.add(input)).thenReturn(input);

        Firestation result = service.create(input);

        assertThat(result).isSameAs(input);
        verify(firestationRepository).findByAddressAndByStation("1509 Culver St", 3);
        verify(firestationRepository).add(input);
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void create_shouldThrowBadRequestWhenAddressMissing() {
        Firestation input = new Firestation(null, 1);

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("L'adresse doit être renseignée.");

        verify(firestationRepository, never()).findByAddressAndByStation(any(), any(Integer.class));
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void create_shouldThrowBadRequestWhenStationNull() {
        Firestation input = new Firestation("1509 Culver St", null);

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Le numéro de la station doit être renseigné.");

        verify(firestationRepository, never()).findByAddressAndByStation(any(), any(Integer.class));
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void create_shouldThrowBadRequestWhenStationLessThanOne() {
        Firestation input = new Firestation("1509 Culver St", 0);

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Le numéro de la station doit être un entier strictement supérieur à zéro.");

        verify(firestationRepository, never()).findByAddressAndByStation(any(), any(Integer.class));
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void create_shouldThrowConflictWhenExists() {
        Firestation input = new Firestation("1509 Culver St", 3);
        when(firestationRepository.findByAddressAndByStation("1509 Culver St", 3)).thenReturn(Optional.of(new Firestation()));

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Une caserne avec la même adresse et le même numéro de station a déjà été ajoutée.");

        verify(firestationRepository).findByAddressAndByStation("1509 Culver St", 3);
        verify(firestationRepository, never()).add(any());
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
    void update_shouldThrowBadRequestWhenAddressMissing() {
        Firestation updated = new Firestation(" ", 2);

        assertThatThrownBy(() -> service.update(updated))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("L'adresse doit être renseignée.");

        verify(firestationRepository, never()).update(any());
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void update_shouldThrowBadRequestWhenStationNull() {
        Firestation updated = new Firestation("1509 Culver St", null);

        assertThatThrownBy(() -> service.update(updated))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Le numéro de la station doit être renseigné.");

        verify(firestationRepository, never()).update(any());
        verifyNoMoreInteractions(firestationRepository);
    }

    @Test
    void update_shouldThrowBadRequestWhenStationLessThanOne() {
        Firestation updated = new Firestation("1509 Culver St", 0);

        assertThatThrownBy(() -> service.update(updated))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Le numéro de la station doit être un entier strictement supérieur à zéro.");

        verify(firestationRepository, never()).update(any());
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