package com.safetynetalerts.controller.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.service.crud.FirestationCrudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FirestationCrudController.class)
class FirestationCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FirestationCrudService firestationService;

    @Test
    void postFirestation_shouldCreateMapping() throws Exception {
        Firestation fs = new Firestation("1509 Culver St", 3);

        when(firestationService.create(any(Firestation.class))).thenReturn(fs);

        // Act + Assert
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs)))
                .andExpect(status().isCreated());

        verify(firestationService).create(any(Firestation.class));
    }

    @Test
    void putFirestation_shouldUpdateMapping() throws Exception {
        Firestation fs = new Firestation("1509 Culver St", 2);

        when(firestationService.update(any(Firestation.class))).thenReturn(true);

        // Act + Assert
        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs)))
                .andExpect(status().isNoContent());

        verify(firestationService).update(any(Firestation.class));
    }

    @Test
    void deleteFirestation_shouldDeleteByAddress() throws Exception {
        when(firestationService.delete("1509 Culver St")).thenReturn(true);

        // Act + Assert
        mockMvc.perform(delete("/firestation")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isNoContent());

        verify(firestationService).delete("1509 Culver St");
    }
}