package com.safetynetalerts.controller;

import com.safetynetalerts.dto.flood.FloodAddressDto;
import com.safetynetalerts.service.FloodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flood/stations")
public class FloodController {

    private static final Logger logger = LoggerFactory.getLogger(FloodController.class);

    private final FloodService floodService;

    public FloodController(FloodService floodService) {
        this.floodService = floodService;
    }

    @GetMapping
    public List<FloodAddressDto> getFloodInfo(@RequestParam List<Integer> stations) {
        logger.info("Requête reçue GET /flood/stations avec stations={}", stations);

        List<FloodAddressDto> response = floodService.getFloodInfoByStations(stations);

        logger.info("Réponse envoyée pour stations={} : {}", stations, response);

        return response;
    }
}
