package com.safetynetalerts.service;

import com.safetynetalerts.dto.personinfo.PersonInfoResponseDto;

public interface PersonInfoService {
    PersonInfoResponseDto getPersonInfoByLastName(String lastName);
}