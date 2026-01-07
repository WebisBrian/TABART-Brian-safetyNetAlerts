package com.safetynetalerts.repository.firestation;

import com.safetynetalerts.model.Firestation;

import java.util.List;
import java.util.Optional;

/**
 * Repository dédié aux Firestation.
 * Ici : lecture + CRUD Firestation uniquement.
 */
public interface FirestationRepository {

    List<Firestation> findAll();

    Optional<Firestation> findByAddress(String address);

    Optional<Firestation> findByAddressAndByStation(String address, Integer stationNumber);

    Firestation add(Firestation firestation);

    boolean update(Firestation firestation);

    boolean deleteByAddress(String address);
}
