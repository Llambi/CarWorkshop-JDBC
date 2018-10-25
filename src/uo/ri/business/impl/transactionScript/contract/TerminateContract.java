package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;
import uo.ri.persistence.impl.ContracStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que contiene la logica para finalizar un contrato.
 */
public class TerminateContract {
    private ContractDto contractDto;
    private Connection connection;

    public TerminateContract(ContractDto contractDto) {
        this.contractDto = contractDto;
    }

    /**
     * Metodo uqe comprueba los prerequisitos para la finalizacion de un contrato y si los cumple lo finaliza.
     *
     * @return Un Map con la infomacion de la liquidacion si fuera necesaria.
     * @throws BusinessException
     */
    public Map<String, Object> execute() throws BusinessException {
        Map<String, Object> liquidacion = null;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            ContractDto previousContract = GatewayFactory.getContractGateway().findContract(contractDto);
            if (previousContract.status.equalsIgnoreCase(ContracStatus.ACTIVE.toString())) {
                GatewayFactory.getContractGateway().terminateContract(previousContract);
                ContractTypeDto contractTypeDto = GatewayFactory.getContractTypeGateway()
                        .findContractType(previousContract);
                liquidacion = liquidarContrato(previousContract, contractTypeDto);
            } else {
                throw new BusinessException("No se cumple lo requerido para actualizar el contrato.");
            }

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible terminar el contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
        return liquidacion;
    }

    /**
     * Metodo que genera la liquidacion de un contrato.
     *
     * @param previousContrac Contrato que se tiene que liquidar.
     * @param contractTypeDto Tipo de contrato que se tiene que liquidar.
     * @return Un Map con toda la informacion de la liquidacion.
     */
    private Map<String, Object> liquidarContrato(ContractDto previousContrac, ContractTypeDto contractTypeDto) {
        double contractYears = isOneYearWorked(previousContrac);
        Map<String, Object> liquidacion = null;
        if (contractYears >= 1.0) {
            liquidacion = new HashMap<>();
            liquidacion.put("salarioBruto", previousContrac.yearBaseSalary);
            liquidacion.put("indemnizacion", contractTypeDto.compensationDays);
            liquidacion.put("añosContrato", Math.round(contractYears));
            liquidacion.put("total", previousContrac.yearBaseSalary * contractTypeDto.compensationDays
                    * Math.round(contractYears));
        }
        return liquidacion;
    }

    /**
     * Metodo que comprueba si se ha trabajado un año desde el inicio del contrato.
     *
     * @param previousContrac Contrato que se quiere comprobar.
     * @return Un double con el modificador.
     */
    private double isOneYearWorked(ContractDto previousContrac) {
        Date startDate = previousContrac.startDate;
        Date today = Dates.today();
        return Dates.diffDays(startDate, today) / 365.;
    }
}
