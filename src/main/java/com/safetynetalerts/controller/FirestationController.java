package com.safetynetalerts.controller;

import com.safetynetalerts.dto.FirestationCoverageDto;
import com.safetynetalerts.service.FirestationServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    private FirestationServiceImpl firestationService;

    public FirestationController(FirestationServiceImpl firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping
    public FirestationCoverageDto getCoverage(@RequestParam int stationNumber) {
        return firestationService.getCoverageByStation(stationNumber);
    }
}
