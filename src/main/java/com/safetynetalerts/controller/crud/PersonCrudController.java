package com.safetynetalerts.controller.crud;

import com.safetynetalerts.dto.mapper.PersonMapper;
import com.safetynetalerts.dto.response.crud.PersonResponseDto;
import com.safetynetalerts.dto.request.PersonUpsertRequestDto;
import com.safetynetalerts.model.Person;
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
        logger.info("POST /person body={} {}", body.firstName(), body.lastName());
        Person toCreate = PersonMapper.toModel(body);
        Person created = service.create(toCreate);

        PersonResponseDto response = PersonMapper.toDto(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody PersonUpsertRequestDto body) {
        logger.info("PUT /person body={} {}", body.firstName(), body.lastName());
        Person toUpdate = PersonMapper.toModel(body);
        boolean updated = service.update(toUpdate);

        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /person firstName={} lastName={}", firstName, lastName);
        boolean deleted = service.delete(firstName, lastName);

        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}