package uo.ri.business.impl.transactionScript.contract;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContracStatus;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractCategoryGateway;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.ContractTypeGateway;
import uo.ri.persistence.exception.PersistanceException;

/**
 * Clase que contiene la logica para listar todos los contratos.
 */
public class FindAllContracts {
    private final ContractGateway contractGateway =
            GatewayFactory.getContractGateway();
    private final ContractTypeGateway contractTypeGateway =
            GatewayFactory.getContractTypeGateway();
    private final ContractCategoryGateway contractCategoryGateway =
            GatewayFactory.getContractCategoryGateway();
    private Connection connection;
    private Long id;

    public FindAllContracts(Long id) {
        this.id = id;
    }

    /**
     * Metodo que recupera los contratos por mecanico.
     *
     * @return Un Map con los contratos y la informacion tanto de las
     * nominas emitidas de cada uno
     * como las liquidaciones de estos.
     * @throws BusinessException
     */
    public List<ContractDto> execute() throws BusinessException {

        List<ContractDto> contracts = new LinkedList<>();
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<ContractDto> mechanicContracts =
                    contractGateway.findContractByMechanicId(this.id);
            for (ContractDto contractDto : mechanicContracts) {
                ContractTypeDto contractTypeDto =
                        contractTypeGateway
                                .findContractTypeById(contractDto.typeId);
                contractDto.categoryName = contractCategoryGateway
                        .findContractCategoryById(contractDto.categoryId).name;
                contractDto.typeName = contractTypeDto.name;
                contractDto.compensation =
                        liquidarContrato(contractDto, contractTypeDto);
                contracts.add(contractDto);
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
    private double liquidarContrato(ContractDto previousContrac,
                                    ContractTypeDto contractTypeDto) {
        double contractYears = isOneYearWorked(previousContrac);
        double total = 0.;
        if (previousContrac.status
                .equalsIgnoreCase(ContracStatus.FINISHED.toString())
                && contractYears >= 1.0) {
            total = previousContrac.yearBaseSalary *
                    contractTypeDto.compensationDays *
                    Math.round(contractYears);
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
        return Dates.diffDays(today, startDate) / 365.;
    }
}
