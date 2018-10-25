package uo.ri.business.impl;

import uo.ri.business.ContractTypeCRUDService;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.contractType.AddContractType;
import uo.ri.business.impl.transactionScript.contractType.DeleteContractType;
import uo.ri.business.impl.transactionScript.contractType.ListContractType;
import uo.ri.business.impl.transactionScript.contractType.UpdateContractType;

import java.util.Map;

public class ContractTypeCRUDImpl implements ContractTypeCRUDService {
    @Override
    public void addContractType(ContractTypeDto contractTypeDto) throws BusinessException {
        new AddContractType(contractTypeDto).execute();
    }

    @Override
    public void deleteContractType(ContractTypeDto contractTypeDto) throws BusinessException {
        new DeleteContractType(contractTypeDto).execute();
    }

    @Override
    public void updateContractType(ContractTypeDto contractTypeDto) throws BusinessException {
        new UpdateContractType(contractTypeDto).execute();
    }

    @Override
    public Map<ContractTypeDto, Map<String, Object>> findAllContractType() throws BusinessException {
        return new ListContractType().execute();
    }
}
