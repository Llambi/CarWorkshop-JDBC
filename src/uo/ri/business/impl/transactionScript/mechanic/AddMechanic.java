package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.exception.PersistanceException;


public class AddMechanic {
    private final MechanicGateway mechanicGateway = GatewayFactory.getMechanicGateway();
    private MechanicDto mechanic;

    public AddMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() throws BusinessException {
        try {
            try {
                if(mechanicGateway.findMechanicByDni(mechanic.dni)!=null)
                    throw new BusinessException("Este DNI ya existe.");
            }catch (PersistanceException ignored){

            }
            mechanicGateway.addMechanic(mechanic);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible añadir el mecanico.\n\t" + e);
        }
    }
}
