package uo.ri.persistence;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public interface MechanicGateway {

    void addMechanic(MechanicDto mechanic) throws PersistanceException;

    void deleteMechanic(Long mechanic) throws PersistanceException;

    void updateMechanic(MechanicDto mechanic) throws PersistanceException;

    List<MechanicDto> findAllMechanics() throws PersistanceException;

    List<MechanicDto> findAllMechanicsByContractType(ContractTypeDto contractTypeDto) throws PersistanceException;

    MechanicDto findMechanicByDni(String dni) throws PersistanceException;

    MechanicDto findMechanicById(long id) throws PersistanceException;

    List<MechanicDto> findActiveMechanics() throws PersistanceException;

    int findMechanicContracts(Long id) throws PersistanceException;

    int findMechanicInterventions(Long id) throws PersistanceException;

    int findMechanicBreakdowns(Long id) throws PersistanceException;
}
