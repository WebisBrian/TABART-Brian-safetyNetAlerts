package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;
import com.safetynetalerts.repository.SafetyNetDataRepository;
import org.springframework.stereotype.Service;

@Service
public class FirestationCrudServiceImpl implements FirestationCrudService {

    private final SafetyNetDataRepository dataRepository;

    public FirestationCrudServiceImpl(SafetyNetDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public Firestation create(Firestation firestation) {
        return dataRepository.addFirestation(firestation);
    }

    @Override
    public boolean update(Firestation firestation) {
        return dataRepository.updateFirestation(firestation);
    }

    @Override
    public boolean delete(String address) {
        return dataRepository.deleteFirestation(address);
    }
}
