package com.safetynetalerts.service;

import com.safetynetalerts.dto.flood.FloodAddressDto;

import java.util.List;

/**
 * Service métier pour la gestion des informations liées aux casernes de pompiers.
 * Fournit notamment une liste des foyers desservis par une liste de casernes
 * pour l'endpoint /flood.
 */
public interface FloodService {

    /**
     * Renvoie les informations de couverture pour une liste de casernes données.
     *
     * @param stations liste des casernes
     * @return un objet contenant la liste des membres des différents foyers ainsi que des
     * éléments tels que nom, prénom, âge, médicaments et allergies.
     */
    List<FloodAddressDto> getFloodInfoByStations(List<Integer> stations);
}
