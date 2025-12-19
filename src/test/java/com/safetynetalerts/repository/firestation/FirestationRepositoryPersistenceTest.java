package com.safetynetalerts.repository.firestation;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.repository.storage.JsonSafetyNetStorage;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

public class FirestationRepositoryPersistenceTest {

    @TempDir
    Path tempDir;

    private Path writeMinimalJson(Path file) throws Exception {
        // JSON minimal attendu par SafetyNetData (mêmes clés racines)
        String json = """
                {
                  "persons": [],
                  "firestations": [],
                  "medicalrecords": []
                }
                """;
        Files.writeString(file, json, StandardCharsets.UTF_8);
        return file;
    }

    private SafetyNetStore newStore(Path dataFile) {
        // Ici on instancie sans Spring : @Value n'empêche pas l'appel du constructeur
        JsonSafetyNetStorage storage = new JsonSafetyNetStorage(new ObjectMapper(), dataFile.toString());
        SafetyNetStore store = new SafetyNetStore(storage);
        store.init(); // simule @PostConstruct
        return store;
    }

    @Test
    void updateFirestation_shouldPersistAfterRestart() throws Exception {
        // Arrange
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));

        // "Démarrage 1"
        SafetyNetStore store1 = newStore(dataFile);
        InMemoryFirestationRepository repo1 = new InMemoryFirestationRepository(store1);

        // on ajoute un mapping adresse -> station
        repo1.add(new Firestation("1509 Culver St", 1));

        // Act : update station pour la même adresse
        boolean updated = repo1.update(new Firestation("1509 Culver St", 3));

        // "Redémarrage" : on recrée store + repo en relisant le fichier
        SafetyNetStore store2 = newStore(dataFile);
        InMemoryFirestationRepository repo2 = new InMemoryFirestationRepository(store2);

        // Assert
        assertThat(updated).isTrue();
        assertThat(repo2.findByAddress("1509 Culver St")).isPresent();
        assertThat(repo2.findByAddress("1509 Culver St").get().getStation()).isEqualTo(3);
    }

    @Test
    void deleteFirestation_shouldPersistAfterRestart() throws Exception {
        // Arrange
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));

        SafetyNetStore store1 = newStore(dataFile);
        InMemoryFirestationRepository repo1 = new InMemoryFirestationRepository(store1);

        repo1.add(new Firestation("1509 Culver St", 1));

        // Act : delete mapping
        boolean deleted = repo1.deleteByAddress("1509 Culver St");

        // Restart
        SafetyNetStore store2 = newStore(dataFile);
        InMemoryFirestationRepository repo2 = new InMemoryFirestationRepository(store2);

        // Assert
        assertThat(deleted).isTrue();
        assertThat(repo2.findByAddress("1509 Culver St")).isEmpty();
        assertThat(repo2.findAll()).isEmpty();
    }
}
