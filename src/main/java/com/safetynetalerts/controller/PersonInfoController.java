package com.safetynetalerts.controller;

import com.safetynetalerts.dto.personinfo.PersonInfoResponseDto;
import com.safetynetalerts.service.PersonInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personInfo")
public class PersonInfoController {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);

    private final PersonInfoService personInfoService;

    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @GetMapping
    public PersonInfoResponseDto getPersonInfoLastName(@RequestParam String lastName) {

        logger.info("Requête reçue GET /personInfo lastName={}",lastName);

        PersonInfoResponseDto response = personInfoService.getPersonInfoByLastName(lastName);

        logger.info("Réponse envoyée /personInfo persons={}", response.persons().size());

        return response;
    }
}
