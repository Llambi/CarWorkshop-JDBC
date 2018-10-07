package uo.ri.business;

import uo.ri.business.dto.MechanicDto;

import java.util.List;

public interface MechanicCRUDService {

    void addMechanic(MechanicDto mechanic);
    void deleteMechanic(MechanicDto mechanic);
    void updateMechanic(MechanicDto mechanic);
    List<MechanicDto> findAllMechanics();
}
