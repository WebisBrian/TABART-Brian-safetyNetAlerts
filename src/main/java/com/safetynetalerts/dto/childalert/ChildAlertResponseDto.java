package com.safetynetalerts.dto.childalert;

import java.util.List;

/**
 * Réponse de l'endpoint /childAlert pour une adresse donnée.
 * Contient la liste des enfants habitant à cette adresse ainsi que la liste
 * des autres membres du foyer (adultes et éventuels autres enfants).
 */
public class ChildAlertResponseDto {

    // fields
    private List<ChildAlertChildDto> children;
    private List<ChildAlertHouseholdMemberDto> otherHouseholdMembers;

    // constructors
    public ChildAlertResponseDto() {}

    public ChildAlertResponseDto(List<ChildAlertChildDto> children, List<ChildAlertHouseholdMemberDto> otherHouseholdMembers) {
        this.children = children;
        this.otherHouseholdMembers = otherHouseholdMembers;
    }

    // getters and setters
    public List<ChildAlertChildDto> getChildren() {
        return children;
    }

    public void setChildren(List<ChildAlertChildDto> children) {
        this.children = children;
    }

    public List<ChildAlertHouseholdMemberDto> getOtherHouseholdMembers() {
        return otherHouseholdMembers;
    }

    public void setOtherHouseholdMembers(List<ChildAlertHouseholdMemberDto> otherHouseholdMembers) {
        this.otherHouseholdMembers = otherHouseholdMembers;
    }

    // overrides
    @Override
    public String toString() {
        return "ChildAlertResponseDto{" +
                "children=" + children +
                ", otherHouseholdMembers=" + otherHouseholdMembers +
                '}';
    }
}
