package uo.ri.persistence;

import uo.ri.business.dto.MechanicDto;

import java.util.List;

public interface MechanicGateway {

    void addMechanic(MechanicDto mechanic);

    void deleteMechanic(MechanicDto mechanic);

    void updateMechanic(MechanicDto mechanic);

    List<MechanicDto> findAllMechanics();
}
