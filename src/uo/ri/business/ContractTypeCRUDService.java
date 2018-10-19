package uo.ri.business;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;

import java.util.List;
import java.util.Map;

public interface ContractTypeCRUDService {
    void addContractType(ContractTypeDto contractTypeDto);
    void deleteContractType(ContractTypeDto contractTypeDto);
    void updateContractType(ContractTypeDto contractTypeDto);
    Map<ContractTypeDto, List<MechanicDto>> findAllContractType();
    Double getTotalBaseSalary(ContractTypeDto contractTypeDto);
}
