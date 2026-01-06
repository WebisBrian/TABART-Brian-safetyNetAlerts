package com.safetynetalerts.controller;

import com.safetynetalerts.dto.response.fire.FireResponseDto;
import com.safetynetalerts.service.FireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fire")
public class FireController {

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);

    private final FireService fireService;

    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    /**
     * Endpoint GET /fire?address=X
     */
    @GetMapping
    public FireResponseDto getFireInfoByAddress(@RequestParam String address) {
        logger.info("Requête reçue GET /fire avec address={}", address);

        FireResponseDto response = fireService.getFireInfoByAddress(address);

        logger.info("Réponse envoyée pour address={} : {}", address, response);

        return response;
    }
}
