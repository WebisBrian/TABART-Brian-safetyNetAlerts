package com.safetynetalerts.controller.crud;

import com.safetynetalerts.dto.request.FirestationUpsertRequestDto;
import com.safetynetalerts.dto.request.PersonUpsertRequestDto;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.service.crud.PersonCrudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.*;
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
        Person person = Person.create("John", "Boyd", "1509 Culver St",
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
    void create_shouldReturn400WhenFirstNameIsMissing() throws Exception {
        String json = """
        {
          "firstName" : "",
          "lastName" : "Boyd",
          "address" : "1509 Culver St",
          "city" : "Culver",
          "zipCode" : "97451",
          "phoneNumber" : "841",
          "email" : "john@email.com"
        }
        """;

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(personService, never()).create(any());
    }

    @Test
    void validateAndNormalize_shouldThrowWhenFirstNameIsBlank() {
        PersonUpsertRequestDto invalidBody = new PersonUpsertRequestDto("", "Boyd", "1509 Culver St",
                "Culver", "97451", "841", "john@email.com");

        assertThatThrownBy(invalidBody::validateAndNormalize)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Le prénom et le nom doivent être renseignés.");
    }

    @Test
    void validateAndNormalize_shouldThrowWhenCityIsNull() {
        PersonUpsertRequestDto invalidBody = new PersonUpsertRequestDto("John", "Boyd", "1509 Culver St",
                null, "97451", "841", "john@email.com");

        assertThatThrownBy(invalidBody::validateAndNormalize)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("La ville et le code postal doivent être renseignés.");
    }

    @Test
    void validateAndNormalize_shouldThrowWhenEmailIsNull() {
        PersonUpsertRequestDto invalidBody = new PersonUpsertRequestDto("John", "Boyd", "1509 Culver St",
                "Culver", "97451", "841", null);

        assertThatThrownBy(invalidBody::validateAndNormalize)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("L'email doit être renseigné.");
    }

    @Test
    void putPerson_shouldUpdatePerson() throws Exception {
        Person person = Person.create("John", "Boyd", "NEW",
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
    void update_shouldReturn400WhenCityIsNull() throws Exception {
        String json = """
        {
          "firstName" : "John",
          "lastName" : "Boyd",
          "address" : "1509 Culver St",
          "zipCode" : "97451",
          "phoneNumber" : "841",
          "email" : "john@email.com"
        }
        """;

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(personService, never()).update(any());
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

    @Test
    void delete_shouldReturn400WhenFirstNameIsNull() throws Exception {
        String json = """
        {
          "lastName" : "Boyd",
        }
        """;

        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(personService, never()).delete(any(), any());
    }
}