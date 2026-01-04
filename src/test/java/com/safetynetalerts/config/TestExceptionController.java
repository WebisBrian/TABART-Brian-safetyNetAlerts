package com.safetynetalerts.config;

import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.model.exception.ConflictException;
import com.safetynetalerts.model.exception.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur volontairement minimaliste,
 * utilisé uniquement pour tester le GlobalExceptionHandler.
 */
@RestController
class TestExceptionController {

    @GetMapping("/test/bad-request")
    void badRequest() {
        throw new BadRequestException("bad request error");
    }

    @GetMapping("/test/not-found")
    void notFound() {
        throw new NotFoundException("not found error");
    }

    @GetMapping("/test/conflict")
    void conflict() {
        throw new ConflictException("conflict error");
    }

    @GetMapping("/test/runtime")
    void runtime() {
        throw new RuntimeException("unexpected error");
    }
}