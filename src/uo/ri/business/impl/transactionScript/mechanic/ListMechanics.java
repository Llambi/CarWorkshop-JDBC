package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.GatewayFactory;

import java.util.List;

public class ListMechanics {

    public List<MechanicDto> execute() {
       return GatewayFactory.getMechanicGateway().findAllMechanics();
    }
}
