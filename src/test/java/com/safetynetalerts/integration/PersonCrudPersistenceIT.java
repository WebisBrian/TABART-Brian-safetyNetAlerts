package com.safetynetalerts.integration;

import com.safetynetalerts.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test d'intégration : appelle réellement l'API et vérifie l'écriture dans le fichier JSON.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PersonCrudPersistenceIT {

    @TempDir
    static Path tempDir;

    private static Path dataFile;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        // On force l'application à utiliser un fichier "writable" dans un répertoire temporaire (test)
        dataFile = tempDir.resolve("data").resolve("data.json");
        registry.add("safetynet.data.path", () -> dataFile.toString());
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void postPerson_shouldPersistIntoJsonFile() throws Exception {
        // ARRANGE : personne unique
        Person person = Person.create(
                "Integration",
                "TestUser",
                "1 Test Street",
                "Culver",
                "97451",
                "111-222-3333",
                "integration@test.com"
        );

        // ACT : appel HTTP réel
        mockMvc.perform(post("/person")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk());

        // ASSERT : on lit le fichier JSON écrit sur disque et on vérifie qu'il contient la personne
        assertThat(Files.exists(dataFile)).isTrue();

        String json = Files.readString(dataFile, StandardCharsets.UTF_8);

        // Vérif : présence des champs identifiants
        assertThat(json).contains("\"firstName\" : \"Integration\"");
        assertThat(json).contains("\"lastName\" : \"TestUser\"");
        assertThat(json).contains("\"email\" : \"integration@test.com\"");
    }
}