package uo.ri.business.impl.transactionScript.contractType;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListContractType {
    private Connection connection;

    public Map<ContractTypeDto, Map<String, Object>> execute() throws BusinessException {
        Map<ContractTypeDto, Map<String, Object>> mechanicsByContractTypeAndAcumSalary = new HashMap<>();
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);


            List<ContractTypeDto> ContractTypes = GatewayFactory.getContractTypeGateway().findAllContractTypes();

            for (ContractTypeDto contractTypeDto : ContractTypes) {
                Map<String, Object> auxDic = new HashMap<>();

                auxDic.put("mechanic", GatewayFactory.getMechanicGateway().findAllMechanicsByContractType(contractTypeDto));
                auxDic.put("acumSalary", GatewayFactory.getPayrollGateway().getTotalBaseSalary(contractTypeDto));

                mechanicsByContractTypeAndAcumSalary.put(contractTypeDto, auxDic);
            }

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible recuperar los tipos de contratos.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
        return mechanicsByContractTypeAndAcumSalary;
    }
}
