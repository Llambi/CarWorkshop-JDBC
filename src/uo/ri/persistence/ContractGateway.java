package uo.ri.persistence;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public interface ContractGateway {
    List<ContractDto> findContract(ContractTypeDto contractDto) throws PersistanceException;

    List<ContractDto> findContract(MechanicDto mechanicDto) throws PersistanceException;

    ContractDto findContract(ContractDto contractDto) throws PersistanceException;

    void addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) throws PersistanceException;

    void terminateContract(ContractDto previousContrac) throws PersistanceException;

    void updateContract(ContractDto contractDto) throws PersistanceException;

    void deleteContract(ContractDto contractDto) throws PersistanceException;
}
