package com.safetynetalerts.service;

import com.safetynetalerts.dto.childalert.ChildAlertChildDto;
import com.safetynetalerts.dto.childalert.ChildAlertResponseDto;
import com.safetynetalerts.repository.SafetyNetDataRepository;

/**
 * Implémentation du service métier pour l'endpoint /childAlert.
 * Elle utilise SafetyNetDataRepository pour récupérer les données brutes
 * puis applique la logique métier (filtrage par date de naissange, calcul de l'âge, etc.).
 */
public class ChildAlertServiceImpl implements ChildAlertService {

    private final SafetyNetDataRepository dataRepository;

    public ChildAlertServiceImpl(SafetyNetDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public ChildAlertResponseDto getChildrenByAddress(String address) {
        // Implémentation à compléter
        return null;
    }
}
