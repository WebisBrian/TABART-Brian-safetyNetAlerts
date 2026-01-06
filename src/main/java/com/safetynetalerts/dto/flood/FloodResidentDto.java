package com.safetynetalerts.dto.flood;

import java.util.List;

public record FloodResidentDto(
        String firstName,
        String lastName,
        String phone,
        int age,
        List<String> medications,
        List<String> allergies) {
}