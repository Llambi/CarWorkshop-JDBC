package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

public class DeleteMechanic {
    private MechanicDto mechanic;

    public DeleteMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() throws BusinessException {
        try {
            GatewayFactory.getMechanicGateway().deleteMechanic(mechanic);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible eliminar el mecanico.\n\t" + e);
        }
    }
}
