package com.safetynetalerts.service;

import com.safetynetalerts.dto.response.personinfo.PersonInfoResponseDto;

/**
 * Service métier pour la recherche par le nom de famille, des habitants.
 * Fournit notamment une liste de la / des personnes dont le nom de famille est renseigné
 * pour l'endpoint /personInfo.
 */
public interface PersonInfoService {

    /**
     * Renvoie les informations et coordonnées pour une ou plusieurs personnes
     * dont le nom de famille est renseigné.
     *
     * @param lastName nom de famille
     * @return un objet contenant la liste de la / des personnes portant le nom de famille renseigné
     * avec des informations telles que nom, prénom, âge, adresse, médicaments et allergies.
     */
    PersonInfoResponseDto getPersonInfoByLastName(String lastName);
}