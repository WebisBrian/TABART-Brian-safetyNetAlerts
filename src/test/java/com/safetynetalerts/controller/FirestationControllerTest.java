package com.safetynetalerts.controller;

import tools.jackson.databind.ObjectMapper;
import com.safetynetalerts.dto.firestation.CoveredPersonDto;
import com.safetynetalerts.dto.firestation.FirestationCoverageDto;
import com.safetynetalerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FirestationController.class)
class FirestationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FirestationService firestationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCoverage_shouldReturnCoverageForStation() throws Exception {
        // ARRANGE
        CoveredPersonDto john = new CoveredPersonDto("John", "Boyd", "1509 Culver St", "841-874-6512");
        FirestationCoverageDto coverage = new FirestationCoverageDto(List.of(john), 1, 0);

        when(firestationService.getCoverageByStation(1)).thenReturn(coverage);

        // ACT + ASSERT
        mockMvc.perform(get("/firestation").param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfAdults").value(1))
                .andExpect(jsonPath("$.numberOfChildren").value(0))
                .andExpect(jsonPath("$.persons[0].firstName").value("John"));
    }

    @Test
    void getCoverage_shouldReturnEmptyForUnknowStation() throws Exception {
        // ARRANGE
        FirestationCoverageDto emptyCoverage = new FirestationCoverageDto(List.of(), 0, 0);

        when(firestationService.getCoverageByStation(2)).thenReturn(emptyCoverage);

        // ACT + ASSERT
        mockMvc.perform(get("/firestation").param("stationNumber", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty())
                .andExpect(jsonPath("$.numberOfAdults").value(0))
                .andExpect(jsonPath("$.numberOfChildren").value(0));
    }
}