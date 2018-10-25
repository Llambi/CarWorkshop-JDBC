package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

public class UpdateMechanic {

    private MechanicDto mechanic;

    public UpdateMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() throws BusinessException {
        // Procesar
        try {
            GatewayFactory.getMechanicGateway().updateMechanic(mechanic);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible actualizar el mecanico.\n\t" + e);
        }
    }
}
