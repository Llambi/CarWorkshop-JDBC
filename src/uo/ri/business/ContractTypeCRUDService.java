package uo.ri.business;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.ContractTypeDto;

import java.util.List;

public interface ContractTypeCRUDService {
    void addContractType(ContractTypeDto contractTypeDto);
    void deleteContractType(ContractTypeDto contractTypeDto);
    void updateContractType(ContractTypeDto contractTypeDto);
    List<ContractTypeDto> findAllContractType();
}
