package uo.ri.business.impl.transactionScript.contract;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.exception.PersistanceException;

public class FindContractById {
    private final ContractGateway contractGateway = GatewayFactory.getContractGateway();
    private Long id;

    public FindContractById(Long id) {
        this.id = id;
    }

    public ContractDto execute() throws BusinessException {
        try {
            return contractGateway.findContractById(this.id);
        } catch (PersistanceException e) {
            throw new BusinessException("No existe contrato con ese ID.");
        }
    }
}
