package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;


public class AddMechanic {
    private MechanicDto mechanic;

    public AddMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() throws BusinessException {
        try {
            GatewayFactory.getMechanicGateway().addMechanic(mechanic);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible añadir el mecanico.\n\t" + e);
        }
    }
}
