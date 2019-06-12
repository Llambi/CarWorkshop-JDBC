package uo.ri.business.impl.transactionScript.payroll;

import alb.util.jdbc.Jdbc;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteLastPayrollForMechanicId {
    private final PayrollGateway payrollGateway = GatewayFactory.getPayrollGateway();
    private Long id;
    private Connection connection;

    public DeleteLastPayrollForMechanicId(Long id) {
        this.id = id;
    }

    public void excecute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            if (payrollGateway.findPayrollsByMechanicId(this.id).isEmpty())
                throw new BusinessException("El mecanico con el id dado no pose nominas");

            payrollGateway.deletePayrollById(this.id);

            connection.commit();

        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible encontrar la nomina del id dado\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }
}
