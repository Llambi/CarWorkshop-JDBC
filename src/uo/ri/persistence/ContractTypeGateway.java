package uo.ri.persistence;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;

import java.util.List;
import java.util.Map;

public interface ContractTypeGateway {

    void addContractType(ContractTypeDto contracTypeDto);

    void deleteContractType(ContractTypeDto contracTypeDto);

    void updateContractType(ContractTypeDto contracTypeDto);

    Map<ContractTypeDto, List<MechanicDto>> findAllContractTypes();
}
