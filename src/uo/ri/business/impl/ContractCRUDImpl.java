package uo.ri.business.impl;

import uo.ri.business.ContractCrudService;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.contract.*;

import java.util.Date;
import java.util.List;

public class ContractCRUDImpl implements ContractCrudService {


    @Override
    public void addContract(ContractDto c) throws BusinessException {
        new AddContract(c).execute();
    }

    @Override
    public void updateContract(ContractDto contractDto) throws BusinessException {
        new UpdateContract(contractDto).execute();
    }

    @Override
    public void deleteContract(Long id) throws BusinessException {
        new DeleteContract(id).execute();
    }

    @Override
    public void finishContract(Long id, Date endDate) throws BusinessException {
        new TerminateContract(id, endDate).execute();
    }

    @Override
    public ContractDto findContractById(Long id) throws BusinessException {
        return new FindContractById(id).execute();
    }

    @Override
    public List<ContractDto> findContractsByMechanicId(Long id) throws BusinessException {
        return new FindAllContracts(id).execute();
    }

}
