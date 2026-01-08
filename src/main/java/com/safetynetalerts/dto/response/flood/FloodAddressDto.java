package com.safetynetalerts.dto.response.flood;

import com.safetynetalerts.dto.response.common.ResidentWithMedicalInfoDto;

import java.util.List;

public record FloodAddressDto(
        String address,
        List<ResidentWithMedicalInfoDto> residents) {
}