package uo.ri.business.impl.transactionScript.contractCategory;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FindAllContractCategories {
    private Connection connection;

    public List<ContractCategoryDto> execute() throws BusinessException {
        List<ContractCategoryDto> dtos;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            dtos = GatewayFactory.getContractCategoryGateway().findAllContractCategories();

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
        return dtos;
    }
}
