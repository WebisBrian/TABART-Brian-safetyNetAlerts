package com.safetynetalerts.dto.response.personinfo;

import java.util.List;

public record PersonInfoResponseDto(
        List<PersonInfoDto> persons) {
}