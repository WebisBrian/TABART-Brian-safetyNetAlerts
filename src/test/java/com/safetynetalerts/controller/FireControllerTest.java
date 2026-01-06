package com.safetynetalerts.controller;

import com.safetynetalerts.dto.common.ResidentWithMedicalInfoDto;
import com.safetynetalerts.dto.response.fire.FireResponseDto;
import com.safetynetalerts.service.FireService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FireController.class)
class FireControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FireService fireService;

    @Test
    void getFireInfoByAddress_shouldReturnFireInfoForKnownAddress() throws Exception {

        ResidentWithMedicalInfoDto john = new ResidentWithMedicalInfoDto(
                "John",
                "Boyd",
                "841-874-6512",
                41,
                List.of("aznol:350mg"),
                List.of("nillacilan")
        );

        FireResponseDto response = new FireResponseDto(3, List.of(john));

        when(fireService.getFireInfoByAddress("1509 Culver St"))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/fire").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stationNumber").value(3))
                .andExpect(jsonPath("$.residents").isArray())
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.residents[0].age").value(41))
                .andExpect(jsonPath("$.residents[0].medications[0]").value("aznol:350mg"))
                .andExpect(jsonPath("$.residents[0].allergies[0]").value("nillacilan"));
    }

    @Test
    void getFireInfoByAddress_shouldReturnEmptyObjectForUnknownAddress() throws Exception {

        FireResponseDto empty = new FireResponseDto(0, List.of());

        when(fireService.getFireInfoByAddress("UNKNOWN"))
                .thenReturn(empty);

        // Act + Assert
        mockMvc.perform(get("/fire").param("address", "UNKNOWN"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stationNumber").value(0))
                .andExpect(jsonPath("$.residents").isEmpty());
    }
}