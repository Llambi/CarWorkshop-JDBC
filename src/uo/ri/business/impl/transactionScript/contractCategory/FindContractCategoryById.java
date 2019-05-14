package uo.ri.business.impl.transactionScript.contractCategory;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class FindContractCategoryById {
    private Long id;
    private Connection connection;

    public FindContractCategoryById(Long id) {
        this.id = id;
    }

    public ContractCategoryDto execute() throws BusinessException {
        ContractCategoryDto dto = null;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            dto = GatewayFactory.getContractCategoryGateway().findContractCategoryById(id);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible recuperar la categoria de contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
        return dto;
    }
}
