package uo.ri.business.impl;

import uo.ri.business.ContractTypeCrudService;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.contract.FindContractById;
import uo.ri.business.impl.transactionScript.contractType.*;

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
        return new FindContractTypeById(id).execute();
    }

    @Override
    public List<ContractTypeDto> findAllContractTypes() throws BusinessException {
        return new ListContractType().execute();
    }

}
