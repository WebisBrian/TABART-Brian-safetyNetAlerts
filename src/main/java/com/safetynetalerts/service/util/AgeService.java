package com.safetynetalerts.service.util;

import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Service utilitaire chargé du calcul d'âge à partir des dossiers médicaux.
 * Centralise la logique de parsing des dates et évite la duplication entre services métier.
 */
@Service
public class AgeService {

    // Format imposé par le fichier data.json (ex: "03/06/1984")
    private static final DateTimeFormatter BIRTHDATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Clock injectée pour rendre le calcul testable (on peut figer la date en test)
    private final Clock clock;

    public AgeService() {
        this.clock = Clock.systemDefaultZone();
    }

    public AgeService(Clock clock) {
        this.clock = clock;
    }

    /**
     * Calcule l'âge à partir d'une date de naissance au format MM/dd/yyyy.
     *
     * @param birthdate date de naissance (ex: "03/06/1984")
     * @return âge en années
     */
    public int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate, BIRTHDATE_FORMAT);
        LocalDate today = LocalDate.now(clock);
        return Period.between(birthDate, today).getYears();
    }

    /**
     * Renvoie l'âge d'une personne si son dossier médical existe, sinon OptionalInt.empty().
     *
     * @param person personne dont on veut l'âge
     * @param medicalRecords liste complète des dossiers médicaux
     * @return OptionalInt contenant l'âge ou vide si aucun dossier trouvé
     */
    public OptionalInt getAge(Person person, List<MedicalRecord> medicalRecords) {
        Optional<MedicalRecord> recordOpt = medicalRecords.stream()
                .filter(mr -> mr.getFirstName().equals(person.getFirstName())
                        && mr.getLastName().equals(person.getLastName()))
                .findFirst();

        if (recordOpt.isEmpty()) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(calculateAge(recordOpt.get().getBirthdate()));
    }

    /**
     * Détermine si une personne est un enfant selon la règle métier (ici: < 18 ans).
     *
     * @param person personne à vérifier
     * @param medicalRecords liste complète des dossiers médicaux
     * @return true si l'âge est < 18, sinon false
     */
    public boolean isChild(Person person, List<MedicalRecord> medicalRecords) {
        // Si pas de dossier médical, on considère "pas enfant" (évite faux positifs)
        return getAge(person, medicalRecords).orElse(Integer.MAX_VALUE) < 18;
    }
}
