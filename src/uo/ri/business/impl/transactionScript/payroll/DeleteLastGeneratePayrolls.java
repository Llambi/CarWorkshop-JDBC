package uo.ri.business.impl.transactionScript.payroll;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DeleteLastGeneratePayrolls {
    private final PayrollGateway payrollGateway = GatewayFactory.getPayrollGateway();
    private Connection connection;

    public int execute() throws BusinessException {
        int count = 0;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<PayrollDto> payrollDtoList = payrollGateway.findAllPayrolls();
            if (!payrollDtoList.isEmpty()) {
                Date newestDate = payrollDtoList.stream()
                        .map(payrollDto -> payrollDto.date)
                        .max(Date::compareTo).get();

                count = payrollGateway.deletePayrollByDate(newestDate);
            }
            connection.commit();

        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible eliminar las nominas generadas\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }

        return count;
    }
}
