package com.safetynetalerts.controller;

import com.safetynetalerts.dto.childalert.ChildAlertChildDto;
import com.safetynetalerts.dto.childalert.ChildAlertHouseholdMemberDto;
import com.safetynetalerts.dto.childalert.ChildAlertResponseDto;
import com.safetynetalerts.service.ChildAlertService;
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
@WebMvcTest(ChildAlertController.class)
class ChildAlertControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ChildAlertService childAlertService;

    @Test
    void getChildAlert_shouldReturnAlertResponseForAddress() throws Exception {
        // ARRANGE
        ChildAlertChildDto tenley = new ChildAlertChildDto("Tenley", "Boyd", 18);
        ChildAlertHouseholdMemberDto john = new ChildAlertHouseholdMemberDto("John", "Boyd", 41, "841-874-6512");
        ChildAlertResponseDto alert = new ChildAlertResponseDto(List.of(tenley), List.of(john));

        when(childAlertService.getChildrenByAddress("1509 Culver St")).thenReturn(alert);

        // ACT + ASSERT
        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.children[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$.children[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.otherHouseholdMembers[0].firstName").value("John"))
                .andExpect(jsonPath("$.otherHouseholdMembers[0].lastName").value("Boyd"));
    }

    @Test
    void getChildAlert_shouldReturnEmptyAlertResponseForAddressWithoutChildren() throws Exception {
        // ARRANGE
        ChildAlertResponseDto alert = new ChildAlertResponseDto(List.of(), List.of());

        when(childAlertService.getChildrenByAddress("29 15th St")).thenReturn(alert);

        // ACT + ASSERT
        mockMvc.perform(get("/childAlert").param("address", "29 15th St"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.children").isEmpty())
                .andExpect(jsonPath("$.otherHouseholdMembers").isEmpty());
    }
}