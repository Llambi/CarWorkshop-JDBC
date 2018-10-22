package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateContract {
    private ContractDto contractDto;
    private Connection connection;

    public UpdateContract(ContractDto contractDto) {
        this.contractDto = contractDto;
        this.contractDto.endDate = Dates.lastDayOfMonth(this.contractDto.endDate);
    }


    public void execute() {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            if(Dates.isBefore(contractDto.endDate,Dates.today())||contractDto.yearBaseSalary<0.){
                //Lanzar excepcion
            }
            GatewayFactory.getContractGateway().updateContract(contractDto);

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
