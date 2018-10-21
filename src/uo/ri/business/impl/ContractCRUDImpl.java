package uo.ri.business.impl;

import uo.ri.business.ContradtCRUDService;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.impl.transactionScript.contract.AddContract;

import java.util.Map;

public class ContractCRUDImpl implements ContradtCRUDService {
    @Override
    public void addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) {
        new AddContract(mechanicDto, contractTypeDto, contractCategoryDto, contractDto).execute();
    }

    @Override
    public void deleteContract(ContractTypeDto contractTypeDto) {

    }

    @Override
    public void updateContract(ContractTypeDto contractTypeDto) {

    }

    @Override
    public Map<ContractTypeDto, Map<String, Object>> findAllContractType() {
        return null;
    }
}
