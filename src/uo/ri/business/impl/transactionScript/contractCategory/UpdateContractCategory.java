package uo.ri.business.impl.transactionScript.contractCategory;

import alb.util.jdbc.Jdbc;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateContractCategory {
    private ContractCategoryDto dto;
    private Connection connection;

    public UpdateContractCategory(ContractCategoryDto dto) {
        this.dto = dto;
    }

    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            checkData();

            GatewayFactory.getContractCategoryGateway().updateContractCategori(dto);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible actualizar la categoria de contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }

    private void checkData() throws BusinessException, PersistanceException {
        if(GatewayFactory.getContractCategoryGateway().findContractCategoryByName(dto.name)==null)throw new BusinessException("La categoria no existe");
        if(dto.productivityPlus<=0)throw new BusinessException("Plus de productividad no puede ser igual o menor que 0.");
        if(dto.trieniumSalary<=0)throw new BusinessException("Trienio no puede ser igual o menor que 0.");
    }
}
