package com.safetynetalerts.controller.crud;

import com.safetynetalerts.mapper.FirestationMapper;
import com.safetynetalerts.dto.response.crud.FirestationResponseDto;
import com.safetynetalerts.dto.request.FirestationUpsertRequestDto;
import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.service.crud.FirestationCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<FirestationResponseDto> create(@RequestBody FirestationUpsertRequestDto body) {
        FirestationUpsertRequestDto validBody = body.validateAndNormalize();

        logger.info("POST /firestation address={} station={}", validBody.address(), validBody.station());
        Firestation toCreate = FirestationMapper.toModel(validBody);
        Firestation created = service.create(toCreate);

        FirestationResponseDto response = FirestationMapper.toDto(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody FirestationUpsertRequestDto body) {
        FirestationUpsertRequestDto validBody = body.validateAndNormalize();

        logger.info("PUT /firestation address={} station={}", validBody.address(), validBody.station());
        Firestation toUpdate = FirestationMapper.toModel(validBody);

        boolean updated = service.update(toUpdate);

        return updated ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String address) {
        String validAddress = normalizeAddress(address);

        logger.info("DELETE /firestation address={}", validAddress);
        boolean deleted = service.delete(validAddress);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private String normalizeAddress(String address) {
        if (address == null) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }
        String validAddress = address.trim();
        if (validAddress.isEmpty()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }
        return validAddress;
    }
}