package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.GatewayFactory;


public class AddMechanic {
    private MechanicDto mechanic;

    public AddMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() {
        GatewayFactory.getMechanicGateway().addMechanic(mechanic);
    }
}
