package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.GatewayFactory;

public class UpdateMechanic {

    private MechanicDto mechanic;

    public UpdateMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute(){
        // Procesar
        GatewayFactory.getMechanicGateway().updateMechanic(mechanic);
    }
}
