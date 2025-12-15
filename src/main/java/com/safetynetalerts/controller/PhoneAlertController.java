package com.safetynetalerts.controller;

import com.safetynetalerts.service.PhoneAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour l'endpoint /phoneAlert.
 * Il expose l'API HTTP et délègue la logique métier au phoneAlertService.
 */
@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    private final PhoneAlertService phoneAlertService;

    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    /**
     * Endpoint GET /phoneAlert?stationNumber=X
     * Renvoie la liste de téléphones des personnes couvertes par la station.
     */
    @GetMapping
    public List<String> getPhonesByStation(@RequestParam int stationNumber) {
        logger.info("Requête reçue GET /phoneAlert avec stationNumber={}", stationNumber);

        List<String> phones = phoneAlertService.getPhonesByStation(stationNumber);

        logger.info("Réponse envoyée pour stationNumber={} : {}", stationNumber, phones);

        return phones;
    }
}
