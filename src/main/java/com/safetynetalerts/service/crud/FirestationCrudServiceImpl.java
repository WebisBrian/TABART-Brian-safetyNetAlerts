package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.model.exception.BadRequestException;
import com.safetynetalerts.model.exception.ConflictException;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FirestationCrudServiceImpl implements FirestationCrudService {

    private final FirestationRepository firestationRepository;

    public FirestationCrudServiceImpl(final FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    @Override
    public Firestation create(Firestation firestation) {

        if (firestation.getAddress() == null || firestation.getAddress().isBlank()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }

        Integer station = firestation.getStation();
        if (station == null) {
            throw new BadRequestException("Le numéro de la station doit être renseigné.");
        }

        if (station < 1) {
            throw new BadRequestException("Le numéro de la station doit être un entier strictement supérieur à zéro.");
        }

        Optional<Firestation> existingFirestation = firestationRepository.findByAddressAndByStation(firestation.getAddress(), station);
        if (existingFirestation.isPresent()){
            throw new ConflictException("Une caserne avec la même adresse et le même numéro de station a déjà été ajoutée.");
        }

        return firestationRepository.add(firestation);
    }

    @Override
    public boolean update(Firestation firestation) {

        if (firestation.getAddress() == null || firestation.getAddress().isBlank()) {
            throw new BadRequestException("L'adresse doit être renseignée.");
        }

        Integer station = firestation.getStation();
        if (station == null) {
            throw new BadRequestException("Le numéro de la station doit être renseigné.");
        }

        if (station < 1) {
            throw new BadRequestException("Le numéro de la station doit être un entier strictement supérieur à zéro.");
        }

        return firestationRepository.update(firestation);
    }

    @Override
    public boolean delete(String address) {
        return firestationRepository.deleteByAddress(address);
    }
}
