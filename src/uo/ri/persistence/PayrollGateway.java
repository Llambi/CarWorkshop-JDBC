package uo.ri.persistence;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.persistence.exception.PersistanceException;

public interface PayrollGateway {
    Double getTotalBaseSalary(ContractTypeDto contractTypeDto) throws PersistanceException;

    int countPayRolls(ContractDto contractDto) throws PersistanceException;
}
