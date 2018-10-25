package uo.ri.persistence;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.persistence.exception.PersistanceException;

public interface ContractCategoryGateway {
    ContractCategoryDto findContractCategory(ContractCategoryDto contractCategoryDto) throws PersistanceException;
}
