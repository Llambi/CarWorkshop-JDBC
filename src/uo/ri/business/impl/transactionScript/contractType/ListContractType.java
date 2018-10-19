package uo.ri.business.impl.transactionScript.contractType;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListContractType {
    private Connection connection;

    public Map<ContractTypeDto, Map<String, Object>> execute() {
        Map<ContractTypeDto, Map<String, Object>> mechanicsByContractTypeAndAcumSalary = new HashMap<>();
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);


            Map<ContractTypeDto, List<MechanicDto>> mechanicsByContractType = GatewayFactory.getContractTypeGateway().findAllContractTypes();

            for (Map.Entry<ContractTypeDto, List<MechanicDto>> entry : mechanicsByContractType.entrySet()) {
                Map<String, Object> auxDic = new HashMap<>();
                ContractTypeDto contractTypeDto = entry.getKey();
                auxDic.put("mechanic", entry.getValue());
                double acumSalary = GatewayFactory.getPayrollGateway().getTotalBaseSalary(contractTypeDto);
                auxDic.put("acumSalary", acumSalary);
                mechanicsByContractTypeAndAcumSalary.put(contractTypeDto, auxDic);
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(connection);
        }
        return mechanicsByContractTypeAndAcumSalary;
    }
}
