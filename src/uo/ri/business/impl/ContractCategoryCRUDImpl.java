package uo.ri.business.impl;

import java.util.List;

import uo.ri.business.ContractCategoryCrudService;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.contractCategory.AddContractCategory;
import uo.ri.business.impl.transactionScript.contractCategory.DeleteContractCategory;
import uo.ri.business.impl.transactionScript.contractCategory.FindAllContractCategories;
import uo.ri.business.impl.transactionScript.contractCategory.FindContractCategoryById;
import uo.ri.business.impl.transactionScript.contractCategory.UpdateContractCategory;

public class ContractCategoryCRUDImpl
        implements ContractCategoryCrudService {
    @Override
    public void addContractCategory(ContractCategoryDto dto)
            throws BusinessException {
        new AddContractCategory(dto).execute();
    }

    @Override
    public void deleteContractCategory(Long id)
            throws BusinessException {
        new DeleteContractCategory(id).execute();
    }

    @Override
    public void updateContractCategory(ContractCategoryDto dto)
            throws BusinessException {
        new UpdateContractCategory(dto).execute();
    }

    @Override
    public ContractCategoryDto findContractCategoryById(Long id)
            throws BusinessException {
        return new FindContractCategoryById(id).execute();
    }

    @Override
    public List<ContractCategoryDto> findAllContractCategories()
            throws BusinessException {
        return new FindAllContractCategories().execute();
    }
}
