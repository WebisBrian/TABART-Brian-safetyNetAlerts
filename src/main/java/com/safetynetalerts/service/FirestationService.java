package com.safetynetalerts.service;

import com.safetynetalerts.dto.response.firestation.FirestationCoverageDto;

/**
 * Service métier pour la gestion des informations liées aux casernes de pompiers.
 * Fournit notamment la couverture d'une caserne (liste des habitants, nombre d'adultes/enfants)
 * pour l'endpoint /firestation.
 */
public interface FirestationService {

    /**
     * Renvoie les informations de couverture pour une caserne donnée.
     *
     * @param stationNumber numéro de la caserne
     * @return un objet contenant la liste des personnes couvertes et les statistiques
     * (nombre d'adultes et d'enfants)
     */
    FirestationCoverageDto getCoverageByStation(Integer stationNumber);
}
