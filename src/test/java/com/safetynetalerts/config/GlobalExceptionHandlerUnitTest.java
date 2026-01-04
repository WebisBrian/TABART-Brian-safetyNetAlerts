package com.safetynetalerts.config;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerUnitTest {

    @Test
    void handleValidation_shouldBuildDetailFromFieldErrors() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        // mocks
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult br = Mockito.mock(BindingResult.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);

        when(req.getRequestURI()).thenReturn("/test");
        when(ex.getBindingResult()).thenReturn(br);
        when(br.getFieldErrors()).thenReturn(List.of(
                new FieldError("obj", "firstName", "must not be blank"),
                new FieldError("obj", "email", "must be a well-formed email address")
        ));

        var resp = handler.handleValidation(ex, req);

        assertThat(resp.getStatusCode().value()).isEqualTo(400);
        assertThat(resp.getBody().getDetail()).contains("firstName: must not be blank");
        assertThat(resp.getBody().getDetail()).contains("email: must be a well-formed email address");
    }
}