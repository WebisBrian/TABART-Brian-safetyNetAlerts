package com.safetynetalerts.repository.storage;

import com.safetynetalerts.model.SafetyNetData;

/**
 * Couche de stockage (persistance) responsable uniquement du chargement/enregistrement de l'entité SafetyNetData.
 * Logique réservée aux opérations I/O de fichiers.
 */
public interface SafetyNetStorage {

    /**
     * Charge SafetyNetData depuis le fichier JSON configuré.
     * Si le fichier est manquant (ou vide), il doit être initialisé depuis data.json présent dans le classpath.
     */
    SafetyNetData load();

    /**
     * Enregistre SafetyNetData dans le fichier JSON configuré (écriture atomique si possible).
     */
    void save(SafetyNetData data);
}
