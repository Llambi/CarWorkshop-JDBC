package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;

import static uo.ri.conf.GatewayFactory.getMechanicGateway;

public class AddMechanic {
    private MechanicDto mechanic;

    public AddMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() {
        // Procesar
        getMechanicGateway().addMechanic(mechanic);
    }
}
