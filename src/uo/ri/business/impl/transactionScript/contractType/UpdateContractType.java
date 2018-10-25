package uo.ri.business.impl.transactionScript.contractType;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

public class UpdateContractType {
    private ContractTypeDto contractTypeDto;

    public UpdateContractType(ContractTypeDto contractTypeDto) {
        this.contractTypeDto = contractTypeDto;
    }

    public void execute() throws BusinessException {
        try {
            GatewayFactory.getContractTypeGateway().updateContractType(contractTypeDto);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible actualizar el tipo de contrato.\n\t" + e);
        }
    }
}
