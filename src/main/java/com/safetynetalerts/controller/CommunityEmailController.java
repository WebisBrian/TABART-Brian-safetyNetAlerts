package com.safetynetalerts.controller;

import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.service.CommunityEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communityEmail")
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping
    public List<String> getEmails(@RequestParam String city) {

        if (city == null || city.isBlank()) {
            throw new BadRequestException("Le nom de la ville doit être renseigné.");
        }

        logger.info("Requête reçue GET /communityEmail city={}", city);

        List<String> response = communityEmailService.getEmailsByCity(city);

        logger.info("Réponse envoyée /communityEmail city={} emails={}", city, response.size());

        return response;
    }
}
