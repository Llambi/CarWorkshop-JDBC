package uo.ri.persistence;

import uo.ri.business.dto.ContractCategoryDto;

public interface ContractCategoryGateway {
    ContractCategoryDto findContractCategory(ContractCategoryDto contractCategoryDto);
}
