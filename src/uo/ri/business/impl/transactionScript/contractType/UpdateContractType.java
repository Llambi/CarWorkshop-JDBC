package uo.ri.business.impl.transactionScript.contractType;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.GatewayFactory;
import uo.ri.conf.ServiceFactory;

public class UpdateContractType {
    private ContractTypeDto contractTypeDto;

    public UpdateContractType(ContractTypeDto contractTypeDto) {
        this.contractTypeDto = contractTypeDto;
    }

    public void execute() {
        GatewayFactory.getContractTypeGateway().updateContractType(contractTypeDto);
    }
}
