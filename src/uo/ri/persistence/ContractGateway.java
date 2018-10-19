package uo.ri.persistence;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;

import java.util.List;

public interface ContractGateway {
    List<ContractDto> findContract(ContractTypeDto contractDto);
}
