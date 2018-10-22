package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.impl.ContracStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TerminateContract {
    private ContractDto contractDto;
    private Connection connection;

    public TerminateContract(ContractDto contractDto) {
        this.contractDto = contractDto;
    }

    public Map<String, Object> execute() {
        Map<String, Object> liquidacion = null;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            ContractDto previousContract = GatewayFactory.getContractGateway().findContract(contractDto);
            if (previousContract.status.equalsIgnoreCase(ContracStatus.ACTIVE.toString())) {
                GatewayFactory.getContractGateway().terminateContract(previousContract);
                ContractTypeDto contractTypeDto = GatewayFactory.getContractTypeGateway().findContractType(previousContract);
                liquidacion = liquidarContrato(previousContract, contractTypeDto);
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
        return liquidacion;
    }

    private Map<String, Object> liquidarContrato(ContractDto previousContrac, ContractTypeDto contractTypeDto) {
        double contractYears = isOneYearWorked(previousContrac);
        Map<String, Object> liquidacion = null;
        if (contractYears >= 1.0) {
            liquidacion = new HashMap<>();
            liquidacion.put("salarioBruto", contractDto.yearBaseSalary);
            liquidacion.put("indemnizacion", contractTypeDto.compensationDays);
            liquidacion.put("añosContrato", Math.round(contractYears));
            liquidacion.put("total", contractDto.yearBaseSalary * contractTypeDto.compensationDays * Math.round(contractYears));
        }
        return liquidacion;
    }

    private double isOneYearWorked(ContractDto previousContrac) {
        Date startDate = previousContrac.startDate;
        Date today = Dates.today();
        return Dates.diffDays(startDate, today) / 365.;
    }
}
