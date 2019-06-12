package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public class FindActiveMechanics {
    public List<MechanicDto> execute() throws BusinessException {
        try {
            return GatewayFactory.getMechanicGateway().findActiveMechanics();
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible recuperar los mecanicos activos.\n\t" + e);
        }
    }
}
