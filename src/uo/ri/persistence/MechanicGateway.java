package uo.ri.persistence;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public interface MechanicGateway {

    void addMechanic(MechanicDto mechanic) throws PersistanceException;

    void deleteMechanic(MechanicDto mechanic) throws PersistanceException;

    void updateMechanic(MechanicDto mechanic) throws PersistanceException;

    List<MechanicDto> findAllMechanics() throws PersistanceException;

    List<MechanicDto> findAllMechanicsByContractType(ContractTypeDto contractTypeDto) throws PersistanceException;

    MechanicDto findMechanicById(long id) throws PersistanceException;
}
