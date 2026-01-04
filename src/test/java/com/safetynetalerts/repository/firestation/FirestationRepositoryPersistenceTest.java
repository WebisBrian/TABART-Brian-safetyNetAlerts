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
import java.util.List;
import java.util.Optional;

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
    void findByAddress_isCaseInsensitive_and_returnsFirst() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));
        SafetyNetStore store = newStore(dataFile);
        InMemoryFirestationRepository repo = new InMemoryFirestationRepository(store);

        repo.add(new Firestation("1509 Culver St", 1));
        repo.add(new Firestation("1509 culver st", 2)); // same address different case

        Optional<Firestation> opt = repo.findByAddress("1509 CULVER ST");

        assertThat(opt).isPresent();
        assertThat(opt.get().getStation()).isEqualTo(1); // first inserted kept
    }

    @Test
    void findByAddressAndByStation_returnsCorrectEntry_when_multiple() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));
        SafetyNetStore store = newStore(dataFile);
        InMemoryFirestationRepository repo = new InMemoryFirestationRepository(store);

        repo.add(new Firestation("A St", 1));
        repo.add(new Firestation("A St", 3));
        repo.add(new Firestation("B St", 2));

        Optional<Firestation> opt1 = repo.findByAddressAndByStation("A St", 3);
        Optional<Firestation> opt2 = repo.findByAddressAndByStation("A St", 99);

        assertThat(opt1).isPresent();
        assertThat(opt1.get().getStation()).isEqualTo(3);
        assertThat(opt2).isEmpty();
    }

    @Test
    void update_shouldModifyOnlyFirstMatching_and_persistAfterRestart() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));
        SafetyNetStore store1 = newStore(dataFile);
        InMemoryFirestationRepository repo1 = new InMemoryFirestationRepository(store1);

        repo1.add(new Firestation("Shared Addr", 1));
        repo1.add(new Firestation("Shared Addr", 2));

        boolean updated = repo1.update(new Firestation("Shared Addr", 9));
        assertThat(updated).isTrue();

        SafetyNetStore store2 = newStore(dataFile);
        InMemoryFirestationRepository repo2 = new InMemoryFirestationRepository(store2);

        List<Firestation> all = repo2.findAll();
        assertThat(all).isNotEmpty();

        List<Firestation> matches = all.stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase("Shared Addr"))
                .toList();

        assertThat(matches).hasSizeGreaterThanOrEqualTo(1);

        assertThat(matches.get(0).getStation()).isEqualTo(9);

        if (matches.size() > 1) {
            assertThat(matches.get(1).getStation()).isEqualTo(2);
        }
    }

    @Test
    void deleteByAddress_shouldRemoveAllMatching_and_persist() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));
        SafetyNetStore store1 = newStore(dataFile);
        InMemoryFirestationRepository repo1 = new InMemoryFirestationRepository(store1);

        repo1.add(new Firestation("ToRemove", 1));
        repo1.add(new Firestation("ToRemove", 2));
        repo1.add(new Firestation("Keep", 5));

        boolean deleted = repo1.deleteByAddress("toremove");
        assertThat(deleted).isTrue();

        SafetyNetStore store2 = newStore(dataFile);
        InMemoryFirestationRepository repo2 = new InMemoryFirestationRepository(store2);

        assertThat(repo2.findByAddress("ToRemove")).isEmpty();

        assertThat(repo2.findByAddress("Keep")).isPresent();
    }

    @Test
    void findAll_returnsAll_insertedEntries() throws Exception {
        Path dataFile = writeMinimalJson(tempDir.resolve("data.json"));
        SafetyNetStore store = newStore(dataFile);
        InMemoryFirestationRepository repo = new InMemoryFirestationRepository(store);

        repo.add(new Firestation("One", 1));
        repo.add(new Firestation("Two", 2));

        List<Firestation> all = repo.findAll();
        assertThat(all).extracting(Firestation::getAddress).contains("One", "Two");
    }
}
