package uo.ri.business;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;

import java.util.Map;

public interface ContradtCRUDService {

    void addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto);
    void deleteContract(ContractTypeDto contractTypeDto);
    void updateContract(ContractTypeDto contractTypeDto);
    Map<ContractTypeDto, Map<String, Object>> findAllContractType();
}
