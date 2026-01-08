package com.safetynetalerts.controller;

import com.safetynetalerts.dto.response.personinfo.PersonInfoDto;
import com.safetynetalerts.dto.response.personinfo.PersonInfoResponseDto;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.service.PersonInfoService;
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


@WebMvcTest(PersonInfoController.class)
class PersonInfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PersonInfoService personInfoService;

    @Test
    void getPersonInfo_shouldReturnPersonsByLastName() throws Exception {
        PersonInfoDto dto = new PersonInfoDto(
                "John", "Boyd", "1509 Culver St", 41,
                "john@email.com", List.of("aznol:350mg"), List.of("nillacilan")
        );

        when(personInfoService.getPersonInfoByLastName("Boyd"))
                .thenReturn(new PersonInfoResponseDto(List.of(dto)));

        mockMvc.perform(get("/personInfo").param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persons[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.persons[0].age").value(41))
                .andExpect(jsonPath("$.persons[0].medications[0]").value("aznol:350mg"));
    }
}
