package com.safetynetalerts.controller.crud;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Person> create(@RequestBody Person person) {
        logger.info("POST /person body={} {}", person.getFirstName(), person.getLastName());
        return ResponseEntity.ok(service.create(person));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestBody Person person) {
        logger.info("PUT /person body={} {}", person.getFirstName(), person.getLastName());
        boolean updated = service.update(person);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /person firstName={} lastName={}", firstName, lastName);
        boolean deleted = service.delete(firstName, lastName);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}