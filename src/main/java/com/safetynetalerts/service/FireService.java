package com.safetynetalerts.service;

import com.safetynetalerts.dto.response.fire.FireResponseDto;

/**
 * Service métier pour l'endpoint /fire.
 * Permet d'obtenir, pour une adresse donnée, le numéro de la caserne associée
 * ainsi que la liste des résidents avec leurs informations médicales.
 */
public interface FireService {

    /**
     * Renvoie les informations incendie pour une adresse donnée.
     *
     * @param address adresse concernée
     * @return données de la caserne et des résidents, ou un objet vide si l'adresse est inconnue
     */
    FireResponseDto getFireInfoByAddress(String address);
}