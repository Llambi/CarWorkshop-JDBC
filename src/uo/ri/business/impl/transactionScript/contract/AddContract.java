package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.*;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddContract {

    private MechanicDto mechanicDto;
    private ContractTypeDto contractTypeDto;
    private ContractCategoryDto contractCategoryDto;
    private ContractDto contractDto;
    private Connection connection;

    public AddContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) {
        this.mechanicDto = mechanicDto;
        this.contractTypeDto = contractTypeDto;
        this.contractCategoryDto = contractCategoryDto;
        this.contractDto = contractDto;
        // Arreglo de fin de contrato
        this.contractDto.endDate = this.contractDto.endDate == null ? null : Dates.lastDayOfMonth(Dates.subMonths(this.contractDto.endDate, 1));
    }

    public Map<String, Object> execute() {
        // Comprobar si el mecanico tiene contratos en vigor (el ultimo) en la fecha que se da, si no se añade,
        // si lo tiene pasa a extinto y se le calculara la liquidacion
        Map<String,Object> liquidacion = null;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            //Recuperamos los objetos necesarios para la creacion del contrato a partir de los datos facilitados en la ui
            recoveryObjectsState();

            // ultimo contrato del mecanico dado
            List<ContractDto> contractDtoList = GatewayFactory.getContractGateway().findContract(mechanicDto);
            ContractDto previousContrac = contractDtoList.get(contractDtoList.size() - 1);
            previousContrac.endDate = Dates.lastDayOfMonth(Dates.today());

            if (isPreviousContract(previousContrac)) {
                GatewayFactory.getContractGateway().terminateContract(previousContrac);
                liquidacion = liquidarContrato(previousContrac);
            }
            GatewayFactory.getContractGateway().addContract(mechanicDto, contractTypeDto, contractCategoryDto, contractDto);

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

    private void recoveryObjectsState(){
        this.mechanicDto = GatewayFactory.getMechanicGateway().findMechanic(this.mechanicDto);
        this.contractTypeDto = GatewayFactory.getContractTypeGateway().findContractType(this.contractTypeDto);
        this.contractCategoryDto = GatewayFactory.getContractCategoryGateway().findContractCategory(this.contractCategoryDto);
    }

    private Map<String, Object> liquidarContrato(ContractDto previousContrac) {
        double contractYears = isOneYearWorked(previousContrac);
        Map<String,Object> liquidacion = null;
        if(contractYears>=1.0){
            liquidacion = new HashMap<>();
            liquidacion.put("salarioBruto",previousContrac.yearBaseSalary);
            liquidacion.put("indemnizacion",contractTypeDto.compensationDays);
            liquidacion.put("añosContrato",Math.round(contractYears));
            liquidacion.put("total",previousContrac.yearBaseSalary*contractTypeDto.compensationDays*Math.round(contractYears));
        }
        return liquidacion;
    }

                            private double isOneYearWorked(ContractDto previousContrac) {
                                Date startDate = previousContrac.startDate;
                                Date today = Dates.today();
                                return Dates.diffDays(startDate, today)/365.;
                            }

    private boolean isPreviousContract(ContractDto previousContrac) {
        boolean flag = false;
        if (previousContrac.endDate == null) {
            // hay que extiguir contrato y liquidar
            flag = true;

        } else {
            if (Dates.isBefore(contractDto.endDate, previousContrac.endDate)) {
                // hay que extiguir contrato y liquidar
                flag = true;
            }
        }
        return flag;
    }
}
