package com.safetynetalerts.controller;

import com.safetynetalerts.dto.communityemail.CommunityEmailResponseDto;
import com.safetynetalerts.service.CommunityEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/communityEmail")
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping
    public CommunityEmailResponseDto getEmails(@RequestParam String city) {

        logger.info("Requête reçue GET /communityEmail city={}", city);

        CommunityEmailResponseDto response = communityEmailService.getEmailsByCity(city);

        logger.info("Réponse envoyée /communityEmail city={} emails={}", city, response.getEmails().size());

        return response;
    }
}
