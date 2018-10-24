package uo.ri.business;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;

import java.util.Map;

public interface ContractCRUDService {

    Map<String, Object> addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) throws BusinessException;

    void deleteContract(ContractDto contractDto) throws BusinessException;

    void updateContract(ContractDto contractDto) throws BusinessException;

    Map<ContractDto, Map<String, Object>> findAllContract(MechanicDto mechanicDto) throws BusinessException;

    Map<String, Object> terminateContract(ContractDto contractDto) throws BusinessException;
}
