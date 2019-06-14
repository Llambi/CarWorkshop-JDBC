package uo.ri.business.impl.transactionScript.contract;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.exception.PersistanceException;

public class FindContractById {
    private final ContractGateway contractGateway =
            GatewayFactory.getContractGateway();
    private Long id;
    private Connection connection;

    public FindContractById(Long id) {
        this.id = id;
    }

    public ContractDto execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            return contractGateway.findContractById(this.id);
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException
                        ("No existe contrato con ese ID.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }
}
