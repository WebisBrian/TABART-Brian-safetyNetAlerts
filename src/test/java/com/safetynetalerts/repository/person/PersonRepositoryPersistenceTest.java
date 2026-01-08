package com.safetynetalerts.repository.person;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.storage.JsonSafetyNetStorage;
import com.safetynetalerts.repository.store.SafetyNetStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

public class PersonRepositoryPersistenceTest {

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
    void updatePerson_shouldPersistAfterRestart() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));

        SafetyNetStore store = newStore(dataFile);
        InMemoryPersonRepository repo1 = new InMemoryPersonRepository(store);

        Person person = repo1.add(Person.create(
                "José",
                "Beauvais",
                "1509 Culver St",
                "Culver",
                "97451",
                "841",
                ""));

        // Act : mise à jour (email)
        person.updateContactInfo(
                "1509 Culver St",
                "Culver",
                "97451",
                "841",
                "josé@email.com"
        );
        boolean updated = repo1.update(person);

        // Restart
        SafetyNetStore store2 = newStore(dataFile);
        InMemoryPersonRepository repo2 = new InMemoryPersonRepository(store2);

        // Assert
        assertThat(updated).isTrue();
        assertThat(repo2.findByName("José", "Beauvais")).isPresent();
        assertThat(repo2.findByName("José", "Beauvais").get().getEmail()).isEqualTo("josé@email.com");
    }

    @Test
    void deletePerson_shouldPersistAfterRestart() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));

        SafetyNetStore store1 = newStore(dataFile);
        InMemoryPersonRepository repo1 = new InMemoryPersonRepository(store1);

        repo1.add(Person.create(
                "José",
                "Beauvais",
                "1509 Culver St",
                "Culver",
                "97451",
                "841",
                ""
        ));

        // Act
        boolean deleted = repo1.delete("José", "Beauvais");

        // Restart
        SafetyNetStore store2 = newStore(dataFile);
        InMemoryPersonRepository repo2 = new InMemoryPersonRepository(store2);

        // Assert
        assertThat(deleted).isTrue();
        assertThat(repo2.findByName("José", "Beauvais")).isEmpty();
        assertThat(repo2.findAll()).isEmpty();
    }
}
