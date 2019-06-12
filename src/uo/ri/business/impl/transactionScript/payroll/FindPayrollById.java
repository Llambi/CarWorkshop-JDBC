package uo.ri.business.impl.transactionScript.payroll;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class FindPayrollById {
    private Long id;
    private Connection connection;
    private PayrollGateway payrollGateway = GatewayFactory.getPayrollGateway();

    public FindPayrollById(Long id) {
        this.id = id;
    }

    public PayrollDto execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            PayrollDto payrollDto = payrollGateway.findPayrollsById(this.id);

            connection.commit();

            return payrollDto;
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
