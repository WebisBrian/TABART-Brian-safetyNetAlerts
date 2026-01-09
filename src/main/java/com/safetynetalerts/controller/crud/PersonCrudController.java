package com.safetynetalerts.controller.crud;

import com.safetynetalerts.mapper.PersonMapper;
import com.safetynetalerts.dto.response.crud.PersonResponseDto;
import com.safetynetalerts.dto.request.PersonUpsertRequestDto;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.service.crud.PersonCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonCrudController {

    private static final Logger logger = LoggerFactory.getLogger(PersonCrudController.class);

    private final PersonCrudService service;

    public PersonCrudController(PersonCrudService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PersonResponseDto> create(@RequestBody PersonUpsertRequestDto body) {
        PersonUpsertRequestDto validBody = body.validateAndNormalize();

        logger.info("POST /person body={} {}", validBody.firstName(), validBody.lastName());
        Person toCreate = PersonMapper.toModel(validBody);
        Person created = service.create(toCreate);

        PersonResponseDto response = PersonMapper.toDto(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody PersonUpsertRequestDto body) {
        PersonUpsertRequestDto validBody = body.validateAndNormalize();

        logger.info("PUT /person body={} {}", validBody.firstName(), validBody.lastName());
        Person toUpdate = PersonMapper.toModel(validBody);
        boolean updated = service.update(toUpdate);

        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String firstName, @RequestParam String lastName) {
        String validFirstName = requireNonBlank(firstName);
        String validLastName = requireNonBlank(lastName);

        logger.info("DELETE /person firstName={} lastName={}", validFirstName, validLastName);
        boolean deleted = service.delete(validFirstName, validLastName);

        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private String requireNonBlank(String value) {
        if (value == null) {
            throw new BadRequestException("Le prénom et le nom doivent être renseignés.");
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new BadRequestException("Le prénom et le nom doivent être renseignés.");
        }
        return trimmed;
    }
}