package com.safetynetalerts.dto.childalert;

import java.util.List;

/**
 * Réponse de l'endpoint /childAlert pour une adresse donnée.
 * Contient la liste des enfants habitant à cette adresse ainsi que la liste
 * des autres membres du foyer (adultes et éventuels autres enfants).
 */
public record ChildAlertResponseDto(
        List<ChildAlertChildDto> children,
        List<ChildAlertHouseholdMemberDto> otherHouseholdMembers) {
}