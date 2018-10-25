package uo.ri.business.impl.transactionScript.contractType;

import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

/**
 * Clase que contiene la logica para la creacion de un tipo de contrato.
 */
public class AddContractType {
    private ContractTypeDto contractTypeDto;

    public AddContractType(ContractTypeDto contractTypeDto) {

        this.contractTypeDto = contractTypeDto;
    }

    /**
     * Metodo que crea un nuevo tipo de contrato si es posible.
     *
     * @throws BusinessException
     */
    public void execute() throws BusinessException {
        try {
            GatewayFactory.getContractTypeGateway().addContractType(contractTypeDto);
        } catch (PersistanceException e) {
            throw new BusinessException("Error al actualizar el tipo de contrato.\n\t" + e);
        }
    }
}
