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

            if (mechanicGateway.findMechanicById(idMecanico) != null)
                throw new BusinessException("El mecanico no existe.");
            if (mechanicGateway.findMechanicContracts(idMecanico) > 0)
                throw new BusinessException("El mecanico tiene contratos asignados.");
            if (mechanicGateway.findMechanicInterventions(idMecanico) > 0)
                throw new BusinessException("El mecanico tiene contratos asignados.");
            if (mechanicGateway.findMechanicBreakdowns(idMecanico) > 0)
                throw new BusinessException("El mecanico tiene contratos asignados.");

            mechanicGateway.deleteMechanic(idMecanico);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible eliminar el mecanico.\n\t" + e);
        }
    }
}
