package uo.ri.business.impl.transactionScript.mechanic;

import alb.util.jdbc.Jdbc;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteMechanic {
    private final MechanicGateway mechanicGateway = GatewayFactory.getMechanicGateway();
    private Long idMecanico;
    private Connection connection;

    public DeleteMechanic(Long idMecanico) {

        this.idMecanico = idMecanico;
    }

    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            if (mechanicGateway.findMechanicById(idMecanico) == null)
                throw new BusinessException("El mecanico no existe.");
            if (mechanicGateway.findMechanicContracts(idMecanico) > 0)
                throw new BusinessException
                        ("El mecanico tiene contratos asignados.");
            if (mechanicGateway.findMechanicInterventions(idMecanico) > 0)
                throw new BusinessException
                        ("El mecanico tiene intervenciones asignadas.");
            if (mechanicGateway.findMechanicBreakdowns(idMecanico) > 0)
                throw new BusinessException
                        ("El mecanico tiene reparaciones asignadas.");

            mechanicGateway.deleteMechanic(idMecanico);
        } catch (PersistanceException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new BusinessException
                    ("Imposible eliminar el mecanico.\n\t" + e.getMessage());
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(connection);
        }
    }
}
