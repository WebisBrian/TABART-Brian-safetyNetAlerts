package com.safetynetalerts.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test d'intégration : vérifie /childAlert sur le dataset réel.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ChildAlertEndpointIT {

    @TempDir
    static Path tempDir;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        // On utilise un fichier writable dédié au test.
        // Ton repo doit copier resources/data.json vers ce chemin au premier load.
        Path dataFile = tempDir.resolve("data").resolve("data.json");
        registry.add("safetynet.data.path", () -> dataFile.toString());
    }

    @Autowired
    MockMvc mockMvc;

    @Test
    void getChildAlert_shouldReturnChildrenForKnownAddress() throws Exception {
        // ACT + ASSERT
        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                // Dans le dataset, Tenley Boyd vit à cette adresse et est enfant (né en 2012)
                .andExpect(jsonPath("$.children").isArray())
                .andExpect(jsonPath("$.children.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.children[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$.children[0].lastName").value("Boyd"))
                // Et il doit aussi y avoir des autres membres du foyer (ex : John Boyd)
                .andExpect(jsonPath("$.otherHouseholdMembers").isArray());
    }
}