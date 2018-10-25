package uo.ri.persistence;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public interface ContractTypeGateway {

    void addContractType(ContractTypeDto contracTypeDto) throws PersistanceException;

    void deleteContractType(ContractTypeDto contracTypeDto) throws PersistanceException;

    void updateContractType(ContractTypeDto contracTypeDto) throws PersistanceException;

    List<ContractTypeDto> findAllContractTypes() throws PersistanceException;

    ContractTypeDto findContractType(ContractTypeDto contractTypeDto) throws PersistanceException;

    ContractTypeDto findContractType(ContractDto contractDto) throws PersistanceException;
}
