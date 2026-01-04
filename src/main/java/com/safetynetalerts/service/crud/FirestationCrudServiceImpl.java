package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.model.exception.ConflictException;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FirestationCrudServiceImpl implements FirestationCrudService {

    private static final String MSG_ADDRESS_REQUIRED = "L'adresse doit être renseignée.";
    private static final String MSG_STATION_REQUIRED = "Le numéro de la station doit être renseigné.";
    private static final String MSG_STATION_POSITIVE = "Le numéro de la station doit être un entier strictement supérieur à zéro.";
    private static final String MSG_CONFLICT = "Une caserne avec la même adresse et le même numéro de station a déjà été ajoutée.";

    private final FirestationRepository firestationRepository;

    public FirestationCrudServiceImpl(final FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    @Override
    public Firestation create(Firestation firestation) {

        Integer station = firestation.getStation();
        validate(firestation, station);

        Optional<Firestation> existingFirestation = firestationRepository.findByAddressAndByStation(firestation.getAddress(), station);
        if (existingFirestation.isPresent()) {
            throw new ConflictException(MSG_CONFLICT);
        }

        return firestationRepository.add(firestation);
    }

    @Override
    public boolean update(Firestation firestation) {

        validate(firestation, firestation.getStation());

        return firestationRepository.update(firestation);
    }

    @Override
    public boolean delete(String address) {
        return firestationRepository.deleteByAddress(address);
    }

    private void validate(Firestation firestation, Integer station) {

        if (firestation.getAddress() == null || firestation.getAddress().isBlank()) {
            throw new BadRequestException(MSG_ADDRESS_REQUIRED);
        }

        if (station == null) {
            throw new BadRequestException(MSG_STATION_REQUIRED);
        }

        if (station < 1) {
            throw new BadRequestException(MSG_STATION_POSITIVE);
        }
    }
}
