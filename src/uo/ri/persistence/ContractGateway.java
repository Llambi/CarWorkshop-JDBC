package uo.ri.persistence;

import uo.ri.business.dto.ContractDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public interface ContractGateway {
    List<ContractDto> findContractByTypeId(Long id) throws PersistanceException;

    List<ContractDto> findContractByMechanicId(Long id) throws PersistanceException;

    List<ContractDto> findContractByMechanicDni(String dni) throws PersistanceException;

    ContractDto findContractById(Long id) throws PersistanceException;

    void addContract(ContractDto contractDto) throws PersistanceException;

    void terminateContract(ContractDto previousContrac) throws PersistanceException;

    void updateContract(ContractDto contractDto) throws PersistanceException;

    void deleteContract(ContractDto contractDto) throws PersistanceException;

    List<ContractDto> findContractByCategoryId(Long id) throws PersistanceException;
}
