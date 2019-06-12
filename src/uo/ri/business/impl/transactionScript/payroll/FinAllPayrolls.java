package uo.ri.business.impl.transactionScript.payroll;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FinAllPayrolls {
    PayrollGateway repo = GatewayFactory.getPayrollGateway();
    private Connection connection;

    public List<PayrollDto> execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<PayrollDto> payrollDtoList = repo.findAllPayrolls();

            connection.commit();

            return payrollDtoList;
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible recuperar nominas.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }
}

