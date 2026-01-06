package com.safetynetalerts.dto.flood;

import java.util.List;

public record FloodAddressDto(
        String address,
        List<FloodResidentDto> residents) {
}