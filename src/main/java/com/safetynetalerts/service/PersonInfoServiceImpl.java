package com.safetynetalerts.service;

import com.safetynetalerts.dto.personinfo.PersonInfoResponseDto;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.service.util.AgeService;
import org.springframework.stereotype.Service;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    private final SafetyNetDataRepository dataRepository;
    private final AgeService ageService;

    public PersonInfoServiceImpl(SafetyNetDataRepository dataRepository, AgeService ageService) {
        this.dataRepository = dataRepository;
        this.ageService = ageService;
    }

    @Override
    public PersonInfoResponseDto getPersonInfo(String lastName) {
        return null;
    }
}
