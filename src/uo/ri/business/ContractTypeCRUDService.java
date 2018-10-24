package uo.ri.business;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;

import java.util.Map;

public interface ContractTypeCRUDService {
    void addContractType(ContractTypeDto contractTypeDto) throws BusinessException;
    void deleteContractType(ContractTypeDto contractTypeDto) throws BusinessException;
    void updateContractType(ContractTypeDto contractTypeDto) throws BusinessException;
    Map<ContractTypeDto, Map<String, Object>> findAllContractType() throws BusinessException;
}
