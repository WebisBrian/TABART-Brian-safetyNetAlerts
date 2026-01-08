package com.safetynetalerts.repository.storage;

import com.safetynetalerts.model.SafetyNetData;

/**
 * Couche de stockage (persistance) responsable uniquement du chargement/enregistrement de l'entité SafetyNetData.
 * Logique réservée aux opérations I/O de fichiers.
 */
public interface SafetyNetStorage {

    /**
     * Charge les données depuis le stockage configuré.
     */
    SafetyNetData load();

    /**
     * Enregistre les données vers le stockage configuré.
     */
    void save(SafetyNetData data);
}
