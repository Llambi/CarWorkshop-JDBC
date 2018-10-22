package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DeleteContract {
    private ContractDto contractDto;
    private Connection connection
            ;

    public DeleteContract(ContractDto contractDto) {
        this.contractDto = contractDto;
    }

    public void execute() {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            //Recuperamos contrato
            contractDto = GatewayFactory.getContractGateway().findContract(contractDto);
            if(!checkMechanicActivity()){
                GatewayFactory.getContractGateway().deleteContract(contractDto);
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

    private boolean checkMechanicActivity() {
        List<BreakdownDto> breakDowns = GatewayFactory.getBreakdownGateway().findMechanicBreakDowns(contractDto);
        int activityInYear=0;
        for (BreakdownDto breakdownDto: breakDowns) {
            if (Dates.isAfter(breakdownDto.date, contractDto.startDate) && Dates.isBefore(breakdownDto.date, contractDto.endDate)) {
                activityInYear++;
            }
        }
        return activityInYear>0;
    }
}
