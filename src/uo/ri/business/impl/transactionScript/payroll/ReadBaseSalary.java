package uo.ri.business.impl.transactionScript.payroll;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.GatewayFactory;

public class ReadBaseSalary {
    private ContractTypeDto contractTypeDto;

    public ReadBaseSalary(ContractTypeDto contractTypeDto) {

        this.contractTypeDto = contractTypeDto;
    }

    public Double execute() {
        return GatewayFactory.getPayrollGateway().getTotalBaseSalary(contractTypeDto);
    }
}
