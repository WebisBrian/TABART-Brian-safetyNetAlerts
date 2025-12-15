package com.safetynetalerts.service;

import com.safetynetalerts.dto.communityemail.CommunityEmailResponseDto;

public interface CommunityEmailService {

    CommunityEmailResponseDto getEmailsByCity(String city);
}
