package uo.ri.business;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;

import java.util.Map;

public interface ContractCRUDService {

    Map<String, Object> addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto);

    void deleteContract(ContractDto contractDto);

    void updateContract(ContractDto contractDto);

    Map<ContractDto, Map<String, Object>> findAllContract(MechanicDto mechanicDto);

    Map<String, Object> terminateContract(ContractDto contractDto);
}
