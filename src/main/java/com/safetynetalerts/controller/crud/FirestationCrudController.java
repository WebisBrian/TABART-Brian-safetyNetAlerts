package com.safetynetalerts.controller.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.service.crud.FirestationCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firestation")
public class FirestationCrudController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationCrudController.class);

    private final FirestationCrudService service;

    public FirestationCrudController(FirestationCrudService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Firestation> create(@RequestBody Firestation firestation) {
        logger.info("POST /firestation address={} station={}", firestation.getAddress(), firestation.getStation());
        return ResponseEntity.ok(service.create(firestation));
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Firestation firestation) {
        logger.info("PUT /firestation address={} station={}", firestation.getAddress(), firestation.getStation());
        boolean updated = service.update(firestation);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String address) {
        logger.info("DELETE /firestation address={}", address);
        boolean deleted = service.delete(address);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}