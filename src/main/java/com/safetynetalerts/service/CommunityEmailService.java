package com.safetynetalerts.service;

import java.util.List;

/**
 * Service métier pour l'endpoint /communityEmail.
 * Permet de récupérer emails des habitants
 * pour une ville donnée.
 */
public interface CommunityEmailService {

    /**
     * Renvoie les emails des habitants pour une ville donnée.
     *
     * @param city ville concernée
     * @return un objet contenant les emails des habitants d'une ville donnée
     */
    List<String> getEmailsByCity(String city);
}
