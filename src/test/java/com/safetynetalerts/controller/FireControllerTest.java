package com.safetynetalerts.controller;

import com.safetynetalerts.dto.fire.FireResponseDto;
import com.safetynetalerts.service.FireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.*;

@RestController
@RequestMapping("/fire")
class FireControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);

    private final FireService fireService;

    public FireControllerTest(FireService fireService) {
        this.fireService = fireService;
    }

    @GetMapping
    public FireResponseDto getFireInfoByAddress(String address) {
        logger.info("Requête reçue GET /fire avec address={}", address);

        FireResponseDto response = fireService.getFireInfoByAddress(address);

        logger.info("Réponse envoyée pour address={} : {}", address, response);

        return response;

    }

}