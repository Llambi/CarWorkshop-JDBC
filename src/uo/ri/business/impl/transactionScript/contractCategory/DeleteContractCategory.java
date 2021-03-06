package uo.ri.business.impl.transactionScript.contractCategory;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractCategoryGateway;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.exception.PersistanceException;

public class DeleteContractCategory {
    private final ContractGateway contractGateway =
            GatewayFactory.getContractGateway();
    private Long id;
    private Connection connection;
    private ContractCategoryGateway categoryGateway =
            GatewayFactory.getContractCategoryGateway();

    public DeleteContractCategory(Long id) {
        this.id = id;
    }

    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            if(categoryGateway.findContractCategoryById(this.id)==null)
                throw new BusinessException("La categoria no existe.");
            if(!contractGateway.findContractByCategoryId(this.id).isEmpty())
                throw new BusinessException
                        ("La categoria tiene contratos asignados.");

            categoryGateway.deleteContractCategory(id);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException
                        ("Imposible eliminar la categoria " +
                                "de contrato.\n\t" + e.getMessage());
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }
}
