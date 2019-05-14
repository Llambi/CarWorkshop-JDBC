package uo.ri.business.impl;

import uo.ri.business.ContractTypeCrudService;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.contractType.AddContractType;
import uo.ri.business.impl.transactionScript.contractType.DeleteContractType;
import uo.ri.business.impl.transactionScript.contractType.ListContractType;
import uo.ri.business.impl.transactionScript.contractType.UpdateContractType;

import java.util.List;

public class ContractTypeCRUDImpl implements ContractTypeCrudService {

    @Override
    public void addContractType(ContractTypeDto dto) throws BusinessException {
        new AddContractType(dto).execute();
    }

    @Override
    public void deleteContractType(Long id) throws BusinessException {
        new DeleteContractType(id).execute();
    }

    @Override
    public void updateContractType(ContractTypeDto dto) throws BusinessException {
        new UpdateContractType(dto).execute();
    }

    @Override
    public ContractTypeDto findContractTypeById(Long id) throws BusinessException {
        return null;
    }

    @Override
    public List<ContractTypeDto> findAllContractTypes() throws BusinessException {
        return new ListContractType().execute();
    }

}
