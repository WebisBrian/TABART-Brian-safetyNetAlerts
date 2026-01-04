package com.safetynetalerts.config;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test-exception")
class TestExceptionControllerWebMvc {

    record Payload(String name) {}

    @PostMapping("/unreadable")
    void unreadable(@RequestBody Payload body) {
        // Spring doit parser le JSON -> si invalide => HttpMessageNotReadableException
    }

    @GetMapping("/type-mismatch")
    void typeMismatch(@RequestParam int value) {}

    @GetMapping("/missing-param")
    void missingParam(@RequestParam String required) {}
}
