package com.safetynetalerts.service.crud;

import com.safetynetalerts.model.Firestation;

public interface FirestationCrudService {

    Firestation create(Firestation firestation);
    boolean update(Firestation firestation);
    boolean delete(String address);
}
