package com.safetynetalerts.repository.storage;

import com.safetynetalerts.model.SafetyNetData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class JsonSafetyNetStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void load_shouldCreateWritableFile_whenMissing() throws Exception {
        // On pointe vers un fichier qui n'existe pas encore
        Path dataFile = tempDir.resolve("data").resolve("data.json");
        assertThat(Files.exists(dataFile)).isFalse();

        // On instancie le storage avec un chemin "writable"
        JsonSafetyNetStorage storage = new JsonSafetyNetStorage(new ObjectMapper(), dataFile.toString());

        // Quand on charge, le storage doit créer le fichier + le remplir depuis resources/data.json
        SafetyNetData data = storage.load();

        // Le fichier doit exister après load()
        assertThat(Files.exists(dataFile)).isTrue();

        // Et les racines doivent être présentes (structure minimale)
        assertThat(data).isNotNull();
        assertThat(data.getPersons()).isNotNull();
        assertThat(data.getFirestations()).isNotNull();
        assertThat(data.getMedicalRecords()).isNotNull();
    }

    @Test
    void load_shouldRecover_whenFileExistsButEmpty() throws Exception {
        // On crée volontairement un fichier vide => cas classique "end-of-input"
        Path dataFile = tempDir.resolve("data.json");
        Files.createFile(dataFile);
        assertThat(Files.size(dataFile)).isZero();

        JsonSafetyNetStorage storage = new JsonSafetyNetStorage(new ObjectMapper(), dataFile.toString());

        // load() doit détecter le fichier vide et recopier resources/data.json
        SafetyNetData data = storage.load();

        assertThat(Files.size(dataFile)).isGreaterThan(0);
        assertThat(data).isNotNull();
        assertThat(data.getPersons()).isNotNull();
        assertThat(data.getFirestations()).isNotNull();
        assertThat(data.getMedicalRecords()).isNotNull();
    }

    @Test
    void save_shouldPersistFile_and_removeTmp() throws Exception {
        // Prépare un fichier de données dans un dossier temporaire
        Path dataFile = tempDir.resolve("data").resolve("data.json");
        JsonSafetyNetStorage storage = new JsonSafetyNetStorage(new ObjectMapper(), dataFile.toString());

        // load() initialise le fichier depuis resources si nécessaire
        SafetyNetData data = storage.load();

        // Modifie légèrement les données pour forcer une écriture (ici on ne précise pas d'objet concret)
        // Appel de save() : doit écrire dans data.json via un .tmp puis déplacer et nettoyer le .tmp
        storage.save(data);

        // Vérifications sur le fichier final
        assertThat(Files.exists(dataFile)).isTrue();
        String content = Files.readString(dataFile, StandardCharsets.UTF_8);
        assertThat(content).contains("\"persons\"");
        assertThat(content).contains("\"firestations\"");
        assertThat(content).contains("\"medicalrecords\"");

        // Le fichier temporaire .tmp ne doit pas exister après le save
        Path tmp = dataFile.resolveSibling(dataFile.getFileName() + ".tmp");
        assertThat(Files.notExists(tmp)).isTrue();
    }
}