package com.safetynetalerts.repository.store;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.SafetyNetData;
import com.safetynetalerts.repository.storage.SafetyNetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SafetyNetStoreTest {

    @Mock
    private SafetyNetStorage storage;

    private SafetyNetStore store;

    private SafetyNetData initialData;

    @BeforeEach
    void setUp() {
        // Prépare des listes non-nulles (SafetyNetStore suppose des listes initialisées)
        initialData = new SafetyNetData();
        initialData.setPersons(new ArrayList<>());
        initialData.setFirestations(new ArrayList<>());
        initialData.setMedicalRecords(new ArrayList<>());

        // ajoute une entrée initiale pour vérifier les modifications
        initialData.getFirestations().add(new Firestation("1509 Culver St", 3));
    }

    @Test
    void init_shouldCallStorageLoad_and_makeDataAvailableForReads() {
        when(storage.load()).thenReturn(initialData);
        store = new SafetyNetStore(storage);

        store.init();

        verify(storage).load();
        Integer count = store.read(d -> d.getFirestations().size());
        assertThat(count).isEqualTo(1);
    }

    @Test
    void read_shouldReturnComputedValue_fromInMemoryData() {
        when(storage.load()).thenReturn(initialData);
        store = new SafetyNetStore(storage);
        store.init();

        String addr = store.read(d -> d.getFirestations().get(0).getAddress());
        assertThat(addr).isEqualTo("1509 Culver St");
    }

    @Test
    void write_shouldApplyChanges_and_callStorageSave_withModifiedData() {
        when(storage.load()).thenReturn(initialData);
        store = new SafetyNetStore(storage);
        store.init();

        store.write(d -> d.getFirestations().add(new Firestation("New Addr", 7)));

        ArgumentCaptor<SafetyNetData> captor = ArgumentCaptor.forClass(SafetyNetData.class);
        verify(storage).save(captor.capture());
        SafetyNetData saved = captor.getValue();

        assertThat(saved.getFirestations()).extracting(Firestation::getAddress).contains("New Addr");
    }

    @Test
    void write_shouldPropagateRuntimeException_whenSaveFails() {
        when(storage.load()).thenReturn(initialData);
        // configure storage.save to throw
        doThrow(new RuntimeException("disk failure")).when(storage).save(any());
        store = new SafetyNetStore(storage);
        store.init();

        assertThrows(RuntimeException.class, () -> store.write(d -> d.getFirestations().add(new Firestation("X", 1))));
        verify(storage).save(any());
    }

    @Test
    void init_shouldPropagateRuntimeException_whenLoadFails() {
        when(storage.load()).thenThrow(new RuntimeException("cannot load"));
        store = new SafetyNetStore(storage);

        assertThrows(RuntimeException.class, store::init);
        verify(storage).load();
    }
}
