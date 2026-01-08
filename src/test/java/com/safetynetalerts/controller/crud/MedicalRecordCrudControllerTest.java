package com.safetynetalerts.controller.crud;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.service.crud.MedicalRecordCrudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordCrudController.class)
class MedicalRecordCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MedicalRecordCrudService medicalRecordService;

    @Test
    void postMedicalRecord_shouldCreateRecord() throws Exception {
        MedicalRecord record = MedicalRecord.create(
                "John", "Boyd", "03/06/1984",
                List.of("aznol:350mg"), List.of("nillacilan")
        );

        when(medicalRecordService.create(any(MedicalRecord.class)))
                .thenReturn(record);

        // Act + Assert
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isCreated());

        verify(medicalRecordService).create(any(MedicalRecord.class));
    }

    @Test
    void putMedicalRecord_shouldUpdateRecord() throws Exception {
        MedicalRecord record = MedicalRecord.create(
                "John", "Boyd", "03/06/1984",
                List.of("new:10mg"), List.of()
        );

        when(medicalRecordService.update(any(MedicalRecord.class)))
                .thenReturn(true);

        // Act + Assert
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isNoContent());

        verify(medicalRecordService).update(any(MedicalRecord.class));
    }

    @Test
    void deleteMedicalRecord_shouldDeleteRecord() throws Exception {
        when(medicalRecordService.delete("John", "Boyd"))
                .thenReturn(true);

        // Act + Assert
        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());

        verify(medicalRecordService).delete("John", "Boyd");
    }
}