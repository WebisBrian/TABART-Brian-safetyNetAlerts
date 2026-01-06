package com.safetynetalerts.dto.response.flood;

import com.safetynetalerts.dto.common.ResidentWithMedicalInfoDto;

import java.util.List;

public record FloodAddressDto(
        String address,
        List<ResidentWithMedicalInfoDto> residents) {
}