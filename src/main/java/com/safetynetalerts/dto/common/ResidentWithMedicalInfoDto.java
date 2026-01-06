package com.safetynetalerts.dto.common;

import java.util.List;

public record ResidentWithMedicalInfoDto(
        String firstName,
        String lastName,
        String phone,
        int age,
        List<String> medications,
        List<String> allergies) {
}
