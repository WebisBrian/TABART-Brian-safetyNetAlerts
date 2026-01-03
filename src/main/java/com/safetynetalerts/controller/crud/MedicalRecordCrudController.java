package com.safetynetalerts.controller.crud;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.service.crud.MedicalRecordCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordCrudController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordCrudController.class);

    private final MedicalRecordCrudService service;

    public MedicalRecordCrudController(MedicalRecordCrudService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MedicalRecord> create(@RequestBody MedicalRecord record) {
        logger.info("POST /medicalRecord {} {}", record.getFirstName(), record.getLastName());
        return ResponseEntity.ok(service.create(record));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestBody MedicalRecord record) {
        logger.info("PUT /medicalRecord {} {}", record.getFirstName(), record.getLastName());
        boolean updated = service.update(record);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /medicalRecord {} {}", firstName, lastName);
        boolean deleted = service.delete(firstName, lastName);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}