package com.safetynetalerts.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestExceptionControllerWebMvc.class)
@Import(GlobalExceptionHandler.class) // on force l'usage de ton RestControllerAdvice
class GlobalExceptionHandlerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldHandleTypeMismatch() throws Exception {
        mockMvc.perform(get("/test-exception/type-mismatch").param("value", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("Paramètre invalide")));
    }

    @Test
    void shouldHandleMissingParam() throws Exception {
        mockMvc.perform(get("/test-exception/missing-param"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("Paramètre manquant")));
    }

    @Test
    void shouldHandleUnreadableJson() throws Exception {
        mockMvc.perform(post("/test-exception/unreadable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("Requête JSON mal formée")));
    }
}