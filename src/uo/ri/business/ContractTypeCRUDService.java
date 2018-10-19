package uo.ri.business;

import uo.ri.business.dto.ContractTypeDto;

import java.util.Map;

public interface ContractTypeCRUDService {
    void addContractType(ContractTypeDto contractTypeDto);
    void deleteContractType(ContractTypeDto contractTypeDto);
    void updateContractType(ContractTypeDto contractTypeDto);
    Map<ContractTypeDto, Map<String, Object>> findAllContractType();
}
