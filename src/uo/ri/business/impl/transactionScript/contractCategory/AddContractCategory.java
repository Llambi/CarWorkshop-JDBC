package uo.ri.business.impl.transactionScript.contractCategory;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractCategoryGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class AddContractCategory {
    private final ContractCategoryGateway categoryGateway = GatewayFactory.getContractCategoryGateway();
    private ContractCategoryDto dto;
    private Connection connection;

    public AddContractCategory(ContractCategoryDto dto) {
        this.dto = dto;
    }

    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            checkData();

            categoryGateway.addContractCategory(dto);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible añadir la categoria de contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }

    private void checkData() throws BusinessException {
        try {
            if (categoryGateway.findContractCategoryByName(this.dto.name) != null)
                throw new BusinessException("Ya existe una categoria con ese nombre.");
        } catch (PersistanceException ignored) {

        }
        if (this.dto.productivityPlus <= 0) throw new BusinessException("Plus de productividad menor o igual a 0.");
        if (this.dto.trieniumSalary <= 0) throw new BusinessException("Plus de productividad menor o igual a 0.");
    }
}
