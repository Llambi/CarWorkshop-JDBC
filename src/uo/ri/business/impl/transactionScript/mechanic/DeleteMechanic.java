package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.GatewayFactory;

public class DeleteMechanic {
    private MechanicDto mechanic;

    public DeleteMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() {
        GatewayFactory.getMechanicGateway().deleteMechanic(mechanic);
    }
}
