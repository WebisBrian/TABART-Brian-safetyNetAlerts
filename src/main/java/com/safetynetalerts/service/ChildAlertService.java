package com.safetynetalerts.service;

import com.safetynetalerts.dto.childalert.ChildAlertResponseDto;

/**
 * Service métier pour l'endpoint /childAlert.
 * Permet de récupérer les enfants et les autres membres du foyer
 * pour une adresse donnée.
 */
public interface ChildAlertService {

    /**
     * Renvoie les enfants et les autres membres du foyer pour une adresse donnée.
     *
     * @param address adresse du foyer
     * @return un objet contenant les listes d'enfants et de membres du foyer
     */
    ChildAlertResponseDto getChildrenByAddress(String address);
}
