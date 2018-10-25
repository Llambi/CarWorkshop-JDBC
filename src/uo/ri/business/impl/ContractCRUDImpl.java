package uo.ri.business.impl;

import uo.ri.business.ContractCRUDService;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.contract.*;

import java.util.Map;

public class ContractCRUDImpl implements ContractCRUDService {
    @Override
    public Map<String, Object> addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) throws BusinessException {
        return new AddContract(mechanicDto, contractTypeDto, contractCategoryDto, contractDto).execute();
    }

    @Override
    public void deleteContract(ContractDto contractDto) throws BusinessException {
        new DeleteContract(contractDto).execute();
    }

    @Override
    public void updateContract(ContractDto contractDto) throws BusinessException {
        new UpdateContract(contractDto).execute();
    }

    @Override
    public Map<ContractDto, Map<String, Object>> findAllContract(MechanicDto mechanicDto) throws BusinessException {
        return new FindAllContracts(mechanicDto).execute();
    }

    @Override
    public Map<String, Object> terminateContract(ContractDto contractDto) throws BusinessException {
        return new TerminateContract(contractDto).execute();
    }

}
