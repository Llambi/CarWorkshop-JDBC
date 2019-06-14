package uo.ri.business.impl.transactionScript.contractType;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

public class FindContractTypeById {
    private Long id;
    private Connection connection;

    public FindContractTypeById(Long id) {
        this.id = id;
    }

    public ContractTypeDto execute() throws BusinessException {
        ContractTypeDto dto = null;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            dto = GatewayFactory.getContractTypeGateway()
                    .findContractTypeById(id);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException
                        ("Imposible recuperar el tipo de " +
                                "contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
        return dto;
    }
}
