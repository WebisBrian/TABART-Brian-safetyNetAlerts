package com.safetynetalerts.controller;

import com.safetynetalerts.dto.childalert.ChildAlertResponseDto;
import com.safetynetalerts.service.ChildAlertService;
import com.safetynetalerts.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    private final ChildAlertService childAlertService;

    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    @GetMapping
    public ChildAlertResponseDto getChildrenByAddress(String address) {
        logger.info("Requête reçue GET /childAlert avec address={}", address);

        ChildAlertResponseDto response = childAlertService.getChildrenByAddress(address);

        logger.info("Réponse envoyée pour address={}: {} enfants et {} autres personnes",
                address,
                response.getChildren().size(),
                response.getOtherHouseholdMembers().size()
        );

        return response;
    }
}
