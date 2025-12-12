package com.safetynetalerts.repository;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.MedicalRecord;
import com.safetynetalerts.model.Person;
import com.safetynetalerts.model.SafetyNetData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Implémentation de {@link SafetyNetDataRepository} qui charge les données
 * de l'application à partir du fichier JSON "data.json" situé dans le classpath.
 *
 * Le fichier est lu une seule fois au démarrage de l'application, puis son
 * contenu est conservé en mémoire dans un objet {@link SafetyNetData}.
 * Les méthodes d'accès renvoient ensuite ces données brutes aux couches supérieures.
 */
@Repository
public class JsonDataRepository implements SafetyNetDataRepository {

    private SafetyNetData safetyNetData;

    public JsonDataRepository() {
        loadData();
    }

    private void loadData() {
        try {
            ClassPathResource resource = new ClassPathResource("data.json");
            InputStream inputStream = resource.getInputStream();

            ObjectMapper mapper = new ObjectMapper();
            this.safetyNetData = mapper.readValue(inputStream, SafetyNetData.class);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du fichier data.json", e);
        }
    }

    @Override
    public List<Person> getAllPersons() {
        return safetyNetData.getPersons();
    }

    public List<Firestation> getAllFirestations() {
        return safetyNetData.getFirestations();
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return safetyNetData.getMedicalrecords();
    }
}
