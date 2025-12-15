package com.safetynetalerts.service;

import com.safetynetalerts.dto.fire.FireResponseDto;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import com.safetynetalerts.service.util.AgeService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FireServiceImpl implements FireService {

    private final SafetyNetDataRepository dataRepository;
    private final AgeService ageService;


    public FireServiceImpl(SafetyNetDataRepository dataRepository, AgeService ageService) {
        this.dataRepository = dataRepository;
        this.ageService = ageService;
    }

    @Override
    public FireResponseDto getFireInfoByAddress(String address) {
        // Implementation goes here
        return null;
    }
}
