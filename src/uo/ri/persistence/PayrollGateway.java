package uo.ri.persistence;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;

public interface PayrollGateway {
    public Double getTotalBaseSalary(ContractTypeDto contractTypeDto);

    int countPayRolls(ContractDto contractDto);
}
