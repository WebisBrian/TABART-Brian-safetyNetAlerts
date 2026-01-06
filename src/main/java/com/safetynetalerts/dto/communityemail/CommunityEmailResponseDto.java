package com.safetynetalerts.dto.communityemail;

import java.util.List;

public record CommunityEmailResponseDto(
        List<String> emails) {
}
