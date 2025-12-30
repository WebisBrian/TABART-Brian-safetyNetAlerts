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
 * Tests unitaires du AgeService.
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
        // ACT
        int age = ageService.calculateAge("01/01/2000");

        // ASSERT
        assertThat(age).isEqualTo(25);
    }

    @Test
    void getAge_shouldReturnAgeWhenMedicalRecordExists() {
        // ARRANGE
        Person person = new Person("John", "Boyd", "", "", "", "", "");
        MedicalRecord record = new MedicalRecord(
                "John",
                "Boyd",
                "03/06/1984",
                List.of(),
                List.of()
        );

        // ACT
        OptionalInt age = ageService.getAge(person, List.of(record));

        // ASSERT
        assertThat(age).isPresent();
        assertThat(age.getAsInt()).isEqualTo(40);
    }

    @Test
    void isChild_shouldReturnTrueForChildAndFalseOtherwise() {
        // ARRANGE
        Person child = new Person("Tenley", "Boyd", "", "", "", "", "");
        Person adult = new Person("John", "Boyd", "", "", "", "", "");

        MedicalRecord childRecord = new MedicalRecord(
                "Tenley",
                "Boyd",
                "02/18/2012",
                List.of(),
                List.of()
        );

        MedicalRecord adultRecord = new MedicalRecord(
                "John",
                "Boyd",
                "03/06/1984",
                List.of(),
                List.of()
        );

        List<MedicalRecord> records = List.of(childRecord, adultRecord);

        // ACT + ASSERT
        assertThat(ageService.isChild(child, records)).isTrue();
        assertThat(ageService.isChild(adult, records)).isFalse();
    }

    @Test
    void isChild_shouldReturnFalseWhenNoMedicalRecord() {
        // ARRANGE
        Person unknown = new Person("Ghost", "Person", "", "", "", "", "");

        // ACT
        boolean isChild = ageService.isChild(unknown, List.of());

        // ASSERT
        assertThat(isChild).isFalse();
    }
}