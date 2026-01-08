package com.safetynetalerts.controller;

import com.safetynetalerts.dto.response.childalert.ChildAlertResponseDto;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.service.ChildAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour l'endpoint /childAlert.
 * Il expose l'API HTTP permettant de récupérer les enfants
 * et les autres membres du foyer pour une adresse donnée.
 */
@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    private final ChildAlertService childAlertService;

    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    /**
     * Endpoint GET /childAlert?address=X
     * Renvoie les enfants et les autres membres du foyer pour l'adresse fournie.
     */
    @GetMapping
    public ChildAlertResponseDto getChildAlert(@RequestParam String address) {

        if (address == null || address.isBlank()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }

        logger.info("Requête reçue GET /childAlert avec address={}", address);

        ChildAlertResponseDto response = childAlertService.getChildrenByAddress(address);

        logger.info("Réponse envoyée pour address={}: {} enfants et {} autres personnes",
                address,
                response.children().size(),
                response.otherHouseholdMembers().size()
        );

        return response;
    }
}
