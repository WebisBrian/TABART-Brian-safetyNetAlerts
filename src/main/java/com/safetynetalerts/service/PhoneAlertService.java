package com.safetynetalerts.service;

import java.util.List;

/**
 * Service métier pour l'endpoint /phoneAlert.
 * Permet de récupérer les numéros de téléphone des habitants couverts par une caserne.
 */
public interface PhoneAlertService {

    /**
     * Renvoie la liste des numéros de téléphone des personnes couvertes par la caserne donnée.
     *
     * @param stationNumber numéro de la caserne
     * @return liste de numéros de téléphone (possiblement vide)
     */
    List<String> getPhonesByStation(Integer stationNumber);
}
