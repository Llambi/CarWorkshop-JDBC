package uo.ri.persistence;

import uo.ri.business.dto.ContractTypeDto;

import java.util.List;

public interface ContractTypeGateway {

    void addContractType(ContractTypeDto contracTypeDto);

    void deleteContractType(ContractTypeDto contracTypeDto);

    void updateContractType(ContractTypeDto contracTypeDto);

    List<ContractTypeDto> findAllContractTypes();
}
