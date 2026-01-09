package com.safetynetalerts.controller.crud;

import com.safetynetalerts.mapper.MedicalRecordMapper;
import com.safetynetalerts.dto.response.crud.MedicalRecordResponseDto;
import com.safetynetalerts.dto.request.MedicalRecordUpsertRequestDto;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.exception.BadRequestException;
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
        MedicalRecordUpsertRequestDto validBody = body.validateAndNormalize();

        logger.info("POST /medicalRecord {} {}", validBody.firstName(), validBody.lastName());
        MedicalRecord toCreate = MedicalRecordMapper.toModel(validBody);
        MedicalRecord created = service.create(toCreate);

        MedicalRecordResponseDto response = MedicalRecordMapper.toDto(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody MedicalRecordUpsertRequestDto body) {
        MedicalRecordUpsertRequestDto validBody = body.validateAndNormalize();

        logger.info("PUT /medicalRecord {} {}", validBody.firstName(), validBody.lastName());
        MedicalRecord toUpdate = MedicalRecordMapper.toModel(validBody);

        boolean updated = service.update(toUpdate);

        return updated ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@RequestParam String firstName, @RequestParam String lastName) {
        String validFirstName = requireNonBlank(firstName, "Le prénom et le nom doivent être renseignés.");
        String validLastName = requireNonBlank(lastName, "Le prénom et le nom doivent être renseignés.");

        logger.info("DELETE /medicalRecord {} {}", validFirstName, validLastName);
        boolean deleted = service.delete(validFirstName, validLastName);

        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private String requireNonBlank(String value, String message) {
        if (value == null) throw new BadRequestException(message);
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new BadRequestException(message);
        return trimmed;
    }
}