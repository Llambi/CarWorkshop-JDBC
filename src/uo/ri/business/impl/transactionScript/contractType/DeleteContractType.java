package uo.ri.business.impl.transactionScript.contractType;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.math.Round;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteContractType {
    private ContractTypeDto contractTypeDto;
    private Connection connection;

    public DeleteContractType(ContractTypeDto contractTypeDto) {
        this.contractTypeDto = contractTypeDto;
    }

    public void execute() {

        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            if(GatewayFactory.getContractGateway().findContract(contractTypeDto).size()>0) {
                GatewayFactory.getContractTypeGateway().deleteContractType(contractTypeDto);
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
    }
}
