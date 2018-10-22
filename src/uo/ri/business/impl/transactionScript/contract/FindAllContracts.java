package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.impl.ContracStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
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
                double liquidacion = liquidarContrato(contractDto, contractTypeDto);
                Map<String, Object> mapa  =new HashMap<>();
                mapa.put("payrolls",numPayRolls);
                mapa.put("liquidacion", liquidacion);
                contracts.put(contractDto,mapa);
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

    private double liquidarContrato(ContractDto previousContrac, ContractTypeDto contractTypeDto) {
        double contractYears = isOneYearWorked(previousContrac);
        double total= 0.;
        Map<String, Object> liquidacion = null;
        if (previousContrac.status.equalsIgnoreCase(ContracStatus.FINISHED.toString()) && contractYears >= 1.0) {
            total =previousContrac.yearBaseSalary * contractTypeDto.compensationDays * Math.round(contractYears);
        }
        return total;
    }

    private double isOneYearWorked(ContractDto previousContrac) {
        Date startDate = previousContrac.startDate;
        Date today = Dates.today();
        return Dates.diffDays(startDate, today) / 365.;
    }
}
