package uo.ri.business.impl.transactionScript.contract;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FindAllContracts {
    private MechanicDto mechanicDto;
    private Connection connection;

    public FindAllContracts(MechanicDto mechanicDto) {
        this.mechanicDto = mechanicDto;
    }

    public Map<ContractDto, Map<String, Object>> execute() {

        Map<ContractDto, Map<String, Object>> contracts = null;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<ContractDto> mechanicContracts = GatewayFactory.getContractGateway().findContract(mechanicDto);
            for (ContractDto contractDto : mechanicContracts){
                ContractTypeDto contractTypeDto = GatewayFactory.getContractTypeGateway().findContractType(contractDto);
                int numPayRolls = GatewayFactory.getPayrollGateway().countPayRolls(contractDto);
                
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
        return contracts;
    }
}
