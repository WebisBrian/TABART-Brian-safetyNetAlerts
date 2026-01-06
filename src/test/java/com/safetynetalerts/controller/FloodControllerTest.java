package com.safetynetalerts.controller;

import com.safetynetalerts.dto.common.ResidentWithMedicalInfoDto;
import com.safetynetalerts.dto.response.flood.FloodAddressDto;
import com.safetynetalerts.service.FloodService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FloodController.class)
class FloodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FloodService floodService;

    @Test
    void getFloodInfo_shouldReturnAddressesAndResidents() throws Exception {

        ResidentWithMedicalInfoDto resident = new ResidentWithMedicalInfoDto(
                "John", "Boyd", "841-874-6512", 41,
                List.of("aznol:350mg"), List.of("nillacilan")
        );

        FloodAddressDto addressDto =
                new FloodAddressDto("1509 Culver St", List.of(resident));

        when(floodService.getFloodInfoByStations(List.of(1)))
                .thenReturn(List.of(addressDto));

        mockMvc.perform(get("/flood/stations").param("stations", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$[0].residents[0].firstName").value("John"));
    }
}
