package com.safetynetalerts.controller;

import com.safetynetalerts.dto.communityemail.CommunityEmailResponseDto;
import com.safetynetalerts.service.CommunityEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommunityEmailController.class)
class CommunityEmailControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CommunityEmailService communityEmailService;

    @Test
    void getEmails_shouldReturnEmails() throws Exception {
        when(communityEmailService.getEmailsByCity("Culver"))
                .thenReturn(new CommunityEmailResponseDto(List.of("a@email.com")));

        mockMvc.perform(get("/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.emails[0]").value("a@email.com"));
    }
}
