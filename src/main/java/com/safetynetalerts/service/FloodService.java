package com.safetynetalerts.service;

import com.safetynetalerts.dto.flood.FloodAddressDto;

import java.util.List;

public interface FloodService {

    List<FloodAddressDto> getFloodInfoByStations(List<Integer> stations);
}
