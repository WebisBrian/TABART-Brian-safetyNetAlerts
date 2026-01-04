package com.safetynetalerts.dto.communityemail;

import java.util.List;

public class CommunityEmailResponseDto {

    // fields
    private List<String> emails;

    // constructors
    public CommunityEmailResponseDto() {
    }

    public CommunityEmailResponseDto(List<String> emails) {
        this.emails = emails;
    }

    // getters and setters
    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    // overrides

    @Override
    public String toString() {
        return "CommunityEmailResponseDto{"
                + "emails=" + emails
                + '}';
    }
}
