package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;
import uo.ri.persistence.impl.ContracStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que contiene la logica para listar todos los contratos.
 */
public class FindAllContracts {
    private MechanicDto mechanicDto;
    private Connection connection;

    public FindAllContracts(MechanicDto mechanicDto) {
        this.mechanicDto = mechanicDto;
    }

    /**
     * Metodo que recupera los contratos por mecanico.
     *
     * @return Un Map con los contratos y la informacion tanto de las nominas emitidas de cada uno
     * como las liquidaciones de estos.
     * @throws BusinessException
     */
    public Map<ContractDto, Map<String, Object>> execute() throws BusinessException {

        Map<ContractDto, Map<String, Object>> contracts = new HashMap<>();
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<ContractDto> mechanicContracts = GatewayFactory.getContractGateway().findContract(mechanicDto);
            for (ContractDto contractDto : mechanicContracts) {
                ContractTypeDto contractTypeDto = GatewayFactory.getContractTypeGateway().findContractType(contractDto);
                int numPayRolls = GatewayFactory.getPayrollGateway().countPayRolls(contractDto);
                double liquidacion = liquidarContrato(contractDto, contractTypeDto);
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("payrolls", numPayRolls);
                mapa.put("liquidacion", liquidacion);
                contracts.put(contractDto, mapa);
            }

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible encontrar todos contratos.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
        return contracts;
    }

    /**
     * Metodoqu que calcula la liquidacion de un contrato ya finalizado.
     *
     * @param previousContrac Contrato del que se quiere calcular la liquidacion.
     * @param contractTypeDto Tipo de contrato del que se quiere calcular la liquidacion.
     * @return Un double con la cantidad de la liquidacion.
     */
    private double liquidarContrato(ContractDto previousContrac, ContractTypeDto contractTypeDto) {
        double contractYears = isOneYearWorked(previousContrac);
        double total = 0.;
        Map<String, Object> liquidacion = null;
        if (previousContrac.status.equalsIgnoreCase(ContracStatus.FINISHED.toString()) && contractYears >= 1.0) {
            total = previousContrac.yearBaseSalary * contractTypeDto.compensationDays * Math.round(contractYears);
        }
        return total;
    }

    /**
     * Metodo que calcula sobre 365 el tiempo trabajado.
     *
     * @param previousContrac Contrato dobre el que se quiere hacer el calculo.
     * @return Un double con el tiempo trabajado.
     */
    private double isOneYearWorked(ContractDto previousContrac) {
        Date startDate = previousContrac.startDate;
        Date today = Dates.today();
        return Dates.diffDays(startDate, today) / 365.;
    }
}
