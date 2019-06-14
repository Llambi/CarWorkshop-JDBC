package uo.ri.business.impl.transactionScript.payroll;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FindPayrollsbyMechanicId {
    private final MechanicGateway mechanicGateway =
            GatewayFactory.getMechanicGateway();
    PayrollGateway payrollGateway = GatewayFactory.getPayrollGateway();
    private Long id;
    private Connection connection;

    public FindPayrollsbyMechanicId(Long id) {

        this.id = id;
    }

    public List<PayrollDto> execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<PayrollDto> payrollDtoList = null;
            if (mechanicGateway.findMechanicById(this.id) != null)
                payrollDtoList = payrollGateway
                        .findPayrollsByMechanicId(this.id);

            connection.commit();

            return payrollDtoList;
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException
                        ("Imposible encontrar la nomina del " +
                                "mecanico dado\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }

    }
}
