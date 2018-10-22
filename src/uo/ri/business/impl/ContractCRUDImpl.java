package uo.ri.business.impl;

import uo.ri.business.ContractCRUDService;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.impl.transactionScript.contract.*;

import java.util.Map;

public class ContractCRUDImpl implements ContractCRUDService {
    @Override
    public Map<String, Object> addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) {
        return new AddContract(mechanicDto, contractTypeDto, contractCategoryDto, contractDto).execute();
    }

    @Override
    public void deleteContract(ContractDto contractDto) {
        new DeleteContract(contractDto).execute();
    }

    @Override
    public void updateContract(ContractDto contractDto) {
        new UpdateContract(contractDto).execute();
    }

    @Override
    public Map<ContractDto, Map<String, Object>> findAllContract(MechanicDto mechanicDto) {
        return new FindAllContracts(mechanicDto).execute();
    }

    @Override
    public Map<String, Object> terminateContract(ContractDto contractDto) {
        return new TerminateContract(contractDto).execute();
    }

}
