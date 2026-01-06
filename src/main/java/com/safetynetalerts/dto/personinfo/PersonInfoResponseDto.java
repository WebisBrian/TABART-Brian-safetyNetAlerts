package com.safetynetalerts.dto.personinfo;

import java.util.List;

public record PersonInfoResponseDto(
        List<PersonInfoDto> persons) {
}