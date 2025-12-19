package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.repository.firestation.FirestationRepository;
import org.springframework.stereotype.Service;

@Service
public class FirestationCrudServiceImpl implements FirestationCrudService {

    private final FirestationRepository firestationRepository;

    public FirestationCrudServiceImpl(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    @Override
    public Firestation create(Firestation firestation) {
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
