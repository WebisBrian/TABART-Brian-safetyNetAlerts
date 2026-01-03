package com.safetynetalerts.controller.crud;

import com.safetynetalerts.dto.crud.medicalrecord.MedicalRecordMapper;
import com.safetynetalerts.dto.crud.medicalrecord.MedicalRecordResponseDto;
import com.safetynetalerts.dto.crud.medicalrecord.MedicalRecordUpsertRequestDto;
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
    public ResponseEntity<MedicalRecordResponseDto> create(@RequestBody MedicalRecordUpsertRequestDto body) {
        logger.info("POST /medicalRecord {} {}", body.getFirstName(), body.getLastName());
        MedicalRecord toCreate = MedicalRecordMapper.toModel(body);
        MedicalRecord created = service.create(toCreate);

        MedicalRecordResponseDto response = MedicalRecordMapper.toDto(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody MedicalRecordUpsertRequestDto body) {
        logger.info("PUT /medicalRecord {} {}", body.getFirstName(), body.getLastName());
        MedicalRecord toUpdate = MedicalRecordMapper.toModel(body);

        boolean updated = service.update(toUpdate);

        return updated ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /medicalRecord {} {}", firstName, lastName);
        boolean deleted = service.delete(firstName, lastName);

        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}