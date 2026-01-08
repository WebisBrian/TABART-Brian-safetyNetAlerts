package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.exception.ConflictException;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FirestationCrudServiceImpl implements FirestationCrudService {

    private static final String MSG_CONFLICT = "Une caserne avec la même adresse et le même numéro de station a déjà été ajoutée.";

    private final FirestationRepository firestationRepository;

    public FirestationCrudServiceImpl(final FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    @Override
    public Firestation create(Firestation firestation) {

        Integer station = firestation.getStation();

        Optional<Firestation> existingFirestation = firestationRepository.findByAddressAndByStation(firestation.getAddress(), station);
        if (existingFirestation.isPresent()) {
            throw new ConflictException(MSG_CONFLICT);
        }

        return firestationRepository.add(firestation);
    }

    @Override
    public boolean update(Firestation firestation) {
        return firestationRepository.update(firestation);
    }

    @Override
    public boolean delete(String address) {
        return firestationRepository.deleteByAddress(address);
    }
}
