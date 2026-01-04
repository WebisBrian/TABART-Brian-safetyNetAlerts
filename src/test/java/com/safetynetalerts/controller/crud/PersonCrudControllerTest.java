package com.safetynetalerts.controller.crud;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.service.crud.PersonCrudService;
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

@WebMvcTest(PersonCrudController.class)
class PersonCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersonCrudService personService;

    @Test
    void postPerson_shouldCreatePerson() throws Exception {
        Person person = new Person("John", "Boyd", "1509 Culver St",
                "Culver", "97451", "841", "john@email.com");

        when(personService.create(any(Person.class))).thenReturn(person);

        // Act + Assert
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated());

        verify(personService).create(any(Person.class));
    }

    @Test
    void putPerson_shouldUpdatePerson() throws Exception {
        Person person = new Person("John", "Boyd", "NEW",
                "Culver", "97451", "999", "new@email.com");

        when(personService.update(any(Person.class))).thenReturn(true);

        // Act + Assert
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isNoContent());

        verify(personService).update(any(Person.class));
    }

    @Test
    void deletePerson_shouldDeletePerson() throws Exception {
        when(personService.delete("John", "Boyd")).thenReturn(true);

        // Act + Assert
        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());

        verify(personService).delete("John", "Boyd");
    }
}