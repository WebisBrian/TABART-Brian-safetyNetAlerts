package com.safetynetalerts.controller;

import com.safetynetalerts.service.PhoneAlertService;
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
@WebMvcTest(PhoneAlertController.class)
class PhoneAlertControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PhoneAlertService phoneAlertService;

    @Test
    void getPhones_shouldReturnPhonesForStation() throws Exception {

        when(phoneAlertService.getPhonesByStation(1))
                .thenReturn(List.of("841-874-6512", "841-874-9845"));

        // Act + Assert
        mockMvc.perform(get("/phoneAlert").param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("841-874-6512"))
                .andExpect(jsonPath("$[1]").value("841-874-9845"));
    }

    @Test
    void getPhones_shouldReturnEmptyListForUnknownStation() throws Exception {

        when(phoneAlertService.getPhonesByStation(3))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/phoneAlert").param("firestation", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }
}