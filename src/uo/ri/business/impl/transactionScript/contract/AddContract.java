package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AddContract {

    private MechanicDto mechanicDto;
    private ContractTypeDto contractTypeDto;
    private ContractCategoryDto contractCategoryDto;
    private ContractDto contractDto;
    private Connection connection;

    public AddContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) {
        //TODO: Completar persistencia necesaria para recuperar datos necesarios
        this.mechanicDto = mechanicDto;
        this.mechanicDto = GatewayFactory.getMechanicGateway().findMechanic(this.mechanicDto);
        this.contractTypeDto = contractTypeDto;
        this.contractTypeDto = GatewayFactory.getContractTypeGateway().findContractType(this.contractTypeDto);
        this.contractCategoryDto = contractCategoryDto;
        this.contractCategoryDto = GatewayFactory.getContractCategoryGateway().findContractCategory(this.contractCategoryDto);
        this.contractDto = contractDto;
        // Arreglo de fin de contrato
        this.contractDto.endDate = this.contractDto.endDate == null ? null : Dates.lastDayOfMonth(Dates.subMonths(this.contractDto.endDate, 1));
    }

    public void execute() {
        // Comprobar si el mecanico tiene contratos en vigor (el ultimo) en la fecha que se da, si no se añade,
        // si lo tiene pasa a extinto y se le calculara la liquidacion

        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            // ultimo contrato del mecanico dado
            List<ContractDto> contractDtoList = GatewayFactory.getContractGateway().findContract(mechanicDto);
            ContractDto previousContrac = contractDtoList.get(contractDtoList.size() - 1);


            if (isPreviousContract(previousContrac)) {
                GatewayFactory.getContractGateway().updateContract(previousContrac);
                liquidarContrato();
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
    }

    private void liquidarContrato() {
        //TODO: Realizar la liquidacion del contrato y mirar que devuelve asumo un resumen de nomina
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
