package uo.ri.business.impl.transactionScript.mechanic;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateMechanic {

    private final MechanicGateway mechanicGateway = GatewayFactory.getMechanicGateway();
    private MechanicDto mechanic;
    private Connection connection;

    public UpdateMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            if (mechanicGateway.findMechanicById(mechanic.id) == null)
                throw new BusinessException("El mecanico no existe.");

            mechanicGateway.updateMechanic(mechanic);
        } catch (PersistanceException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new BusinessException("Imposible actualizar el mecanico.\n\t" + e.getMessage());
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
