package uo.ri.business.impl.transactionScript.mechanic;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;


public class AddMechanic {
    private final MechanicGateway mechanicGateway =
            GatewayFactory.getMechanicGateway();
    private MechanicDto mechanic;
    private Connection connection;

    public AddMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            if (mechanicGateway.findMechanicByDni(mechanic.dni) != null)
                throw new BusinessException("Este DNI ya existe.");

            mechanicGateway.addMechanic(mechanic);
        } catch (PersistanceException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new BusinessException
                    ("Imposible añadir el mecanico.\n\t" + e.getMessage());
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
