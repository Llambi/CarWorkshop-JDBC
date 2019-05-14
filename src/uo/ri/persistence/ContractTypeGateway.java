package uo.ri.persistence;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public interface ContractTypeGateway {

    void addContractType(ContractTypeDto contracTypeDto) throws PersistanceException;

    void deleteContractType(long id) throws PersistanceException;

    void updateContractType(ContractTypeDto contracTypeDto) throws PersistanceException;

    List<ContractTypeDto> findAllContractTypes() throws PersistanceException;

    ContractTypeDto findContractTypeById(long id) throws PersistanceException;

    ContractTypeDto findContractTypeByName(String name) throws PersistanceException;
}
