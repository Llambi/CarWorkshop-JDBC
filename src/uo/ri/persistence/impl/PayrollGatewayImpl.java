package uo.ri.persistence.impl;

import uo.ri.business.ContractTypeCRUDService;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.persistence.PayrollGateway;

public class PayrollGatewayImpl implements PayrollGateway {
    @Override
    public Double getTotalBaseSalary(ContractTypeDto contractTypeDto) {
        //TODO: Hacer recuracion de salario base por contrato
        return null;
    }
}
