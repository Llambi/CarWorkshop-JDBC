package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.exception.PersistanceException;

public class DeleteMechanic {
    private final MechanicGateway mechanicGateway = GatewayFactory.getMechanicGateway();
    private Long idMecanico;

    public DeleteMechanic(Long idMecanico) {

        this.idMecanico = idMecanico;
    }

    public void execute() throws BusinessException {
        try {
            try {
                if (mechanicGateway.findMechanicById(idMecanico).dni != null)
                    throw new BusinessException("El mecanico no existe.");
            } catch (PersistanceException ignored) {

            }
            mechanicGateway.deleteMechanic(idMecanico);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible eliminar el mecanico.\n\t" + e);
        }
    }
}
