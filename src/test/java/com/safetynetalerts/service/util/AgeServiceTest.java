package com.safetynetalerts.service.util;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitaires pour AgeService.
 * Clock figée pour garantir des tests stables dans le temps.
 */
class AgeServiceTest {

    private AgeService ageService;

    @BeforeEach
    void setUp() {
        // Date figée : 2025-01-01
        Clock fixedClock = Clock.fixed(
                Instant.parse("2025-01-01T00:00:00Z"),
                ZoneId.systemDefault()
        );
        ageService = new AgeService(fixedClock);
    }

    @Test
    void calculateAge_shouldReturnCorrectAge() {
        // Act
        int age = ageService.calculateAge("01/01/2000");

        // Assert
        assertThat(age).isEqualTo(25);
    }

    @Test
    void getAge_shouldReturnAgeWhenMedicalRecordExists() {

        Person person = Person.create("John", "Boyd", "", "", "", "", "");
        MedicalRecord record = MedicalRecord.create(
                "John",
                "Boyd",
                "03/06/1984",
                List.of(),
                List.of()
        );

        // Act
        OptionalInt age = ageService.getAge(person, List.of(record));

        // Assert
        assertThat(age).isPresent();
        assertThat(age.getAsInt()).isEqualTo(40);
    }

    @Test
    void isChild_shouldReturnTrueForChildAndFalseOtherwise() {

        Person child = Person.create("Tenley", "Boyd", "", "", "", "", "");
        Person adult = Person.create("John", "Boyd", "", "", "", "", "");

        MedicalRecord childRecord = MedicalRecord.create(
                "Tenley",
                "Boyd",
                "02/18/2012",
                List.of(),
                List.of()
        );

        MedicalRecord adultRecord = MedicalRecord.create(
                "John",
                "Boyd",
                "03/06/1984",
                List.of(),
                List.of()
        );

        List<MedicalRecord> records = List.of(childRecord, adultRecord);

        // Act + Assert
        assertThat(ageService.isChild(child, records)).isTrue();
        assertThat(ageService.isChild(adult, records)).isFalse();
    }

    @Test
    void isChild_shouldReturnFalseWhenNoMedicalRecord() {

        Person unknown = Person.create("Ghost", "Person", "", "", "", "", "");

        // Act
        boolean isChild = ageService.isChild(unknown, List.of());

        // Assert
        assertThat(isChild).isFalse();
    }
}