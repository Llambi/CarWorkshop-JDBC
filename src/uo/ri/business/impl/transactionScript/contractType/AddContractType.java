package uo.ri.business.impl.transactionScript.contractType;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.GatewayFactory;

public class AddContractType {
    private ContractTypeDto contractTypeDto;

    public AddContractType(ContractTypeDto contractTypeDto) {

        this.contractTypeDto = contractTypeDto;
    }

    public void execute() {
        GatewayFactory.getContractTypeGateway().addContractType(contractTypeDto);
    }
}
