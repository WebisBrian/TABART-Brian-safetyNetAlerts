package com.safetynetalerts.repository.medicalrecord;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.repository.storage.JsonSafetyNetStorage;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MedicalRecordRepositoryPersistenceTest {

    @TempDir
    Path tempDir;

    private Path writeMinimalJson(Path file) throws Exception {
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
        JsonSafetyNetStorage storage = new JsonSafetyNetStorage(new ObjectMapper(), dataFile.toString());
        SafetyNetStore store = new SafetyNetStore(storage);
        store.init(); // simule @PostConstruct
        return store;
    }

    @Test
    void updateMedicalRecord_shouldPersistAfterRestart() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));

        SafetyNetStore store1 = newStore(dataFile);
        InMemoryMedicalRecordRepository repo1 = new InMemoryMedicalRecordRepository(store1);

        repo1.add(new MedicalRecord(
                "John", "Boyd",
                "03/06/1984",
                List.of("aznol:350mg"),
                List.of("nillacilan")
        ));

        // Act : mise à jour (médicaments + allergies)
        boolean updated = repo1.update(new MedicalRecord(
                "John", "Boyd",
                "03/06/1984",
                List.of("newmed:10mg"),
                List.of("peanut")
        ));

        // Restart
        SafetyNetStore store2 = newStore(dataFile);
        InMemoryMedicalRecordRepository repo2 = new InMemoryMedicalRecordRepository(store2);

        // Assert
        assertThat(updated).isTrue();
        assertThat(repo2.findByName("John", "Boyd")).isPresent();
        MedicalRecord reloaded = repo2.findByName("John", "Boyd").get();
        assertThat(reloaded.getMedications()).containsExactly("newmed:10mg");
        assertThat(reloaded.getAllergies()).containsExactly("peanut");
    }

    @Test
    void deleteMedicalRecord_shouldPersistAfterRestart() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));

        SafetyNetStore store1 = newStore(dataFile);
        InMemoryMedicalRecordRepository repo1 = new InMemoryMedicalRecordRepository(store1);

        repo1.add(new MedicalRecord(
                "John", "Boyd",
                "03/06/1984",
                List.of("aznol:350mg"),
                List.of("nillacilan")
        ));

        // Act
        boolean deleted = repo1.delete("John", "Boyd");

        // Restart
        SafetyNetStore store2 = newStore(dataFile);
        InMemoryMedicalRecordRepository repo2 = new InMemoryMedicalRecordRepository(store2);

        // Assert
        assertThat(deleted).isTrue();
        assertThat(repo2.findByName("John", "Boyd")).isEmpty();
        assertThat(repo2.findAll()).isEmpty();
    }
}
