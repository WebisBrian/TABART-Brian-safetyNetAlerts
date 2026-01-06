package com.safetynetalerts.controller;

import com.safetynetalerts.dto.response.firestation.FirestationCoverageDto;
import com.safetynetalerts.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour l'endpoint /firestation.
 * Il expose l'API HTTP et délègue la logique métier au FirestationService.
 */
@RestController
@RequestMapping("/firestation")
public class FirestationController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Endpoint GET /firestation?stationNumber=X
     * Renvoie la couverture de la caserne demandée (personnes, nb adultes/enfants).
     */
    @GetMapping
    public FirestationCoverageDto getCoverage(@RequestParam int stationNumber) {
        logger.info("Requête reçue GET /firestation avec stationNumber={}", stationNumber);

        FirestationCoverageDto coverage = firestationService.getCoverageByStation(stationNumber);

        logger.info("Réponse envoyée pour stationNumber={}: {} personnes (adultes={}, enfants={})",
                stationNumber,
                coverage.persons().size(),
                coverage.numberOfAdults(),
                coverage.numberOfChildren()
        );

        return coverage;
    }
}
