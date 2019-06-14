package uo.ri.persistence;

import java.util.List;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.persistence.exception.PersistanceException;

public interface ContractCategoryGateway {
    ContractCategoryDto findContractCategoryById(long id) throws PersistanceException;

    ContractCategoryDto findContractCategoryByName(String name) throws PersistanceException;

    void addContractCategory(ContractCategoryDto dto) throws PersistanceException;

    void deleteContractCategory(Long id) throws PersistanceException;

    void updateContractCategori(ContractCategoryDto dto) throws PersistanceException;

    List<ContractCategoryDto> findAllContractCategories() throws PersistanceException;
}
