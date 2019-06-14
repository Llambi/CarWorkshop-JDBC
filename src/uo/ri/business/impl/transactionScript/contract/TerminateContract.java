package uo.ri.business.impl.transactionScript.contract;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContracStatus;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.ContractTypeGateway;
import uo.ri.persistence.exception.PersistanceException;

/**
 * Clase que contiene la logica para finalizar un contrato.
 */
public class TerminateContract {
    private final ContractGateway contractGateway =
            GatewayFactory.getContractGateway();
    private final ContractTypeGateway contractTypeGateway =
            GatewayFactory.getContractTypeGateway();
    private Long id;
    private Date endDate;
    private Connection connection;

    public TerminateContract(Long id, Date endDate) {
        this.id = id;
        this.endDate = Dates.lastDayOfMonth(endDate);
    }

    /**
     * Metodo uqe comprueba los prerequisitos para la finalizacion
     * de un contrato y si los cumple lo finaliza.
     *
     * @return Un Map con la infomacion de la liquidacion si fuera
     * necesaria.
     * @throws BusinessException
     */
    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            ContractDto previousContract = contractGateway
                    .findContractById(this.id);
            if (previousContract == null)
                throw new BusinessException
                        ("No existe el contrato a finalizar");
            if (Dates.isBefore(this.endDate, previousContract.startDate))
                throw new BusinessException
                        ("La fecha de finalizacion debe ser " +
                                "posterior a la de inicio.");

            previousContract.endDate = this.endDate;
            if (previousContract.status
                    .equalsIgnoreCase(ContracStatus.ACTIVE.toString())) {
                ContractTypeDto contractTypeDto = contractTypeGateway
                        .findContractTypeById(previousContract.typeId);
                previousContract.compensation =
                        liquidarContrato(previousContract, contractTypeDto);
                contractGateway.terminateContract(previousContract);
            } else {
                throw new BusinessException
                        ("El contrato ya esta finalizado.");
            }

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException
                        ("Imposible terminar el contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }

    /**
     * Metodo que genera la liquidacion de un contrato.
     *
     * @param previousContrac Contrato que se tiene que liquidar.
     * @param contractTypeDto Tipo de contrato que se tiene que liquidar.
     * @return Un Map con toda la informacion de la liquidacion.
     */
    private Double liquidarContrato(ContractDto previousContrac,
                                    ContractTypeDto contractTypeDto) {
        double contractYears = isOneYearWorked(previousContrac);
        Double liquidacion = 0D;
        if (contractYears >= 1.0) {
            liquidacion = previousContrac.yearBaseSalary *
                    contractTypeDto.compensationDays
                    * Math.round(contractYears);
        }
        return liquidacion;
    }

    /**
     * Metodo que comprueba si se ha trabajado un año desde el inicio
     * del contrato.
     *
     * @param previousContrac Contrato que se quiere comprobar.
     * @return Un double con el modificador.
     */
    private double isOneYearWorked(ContractDto previousContrac) {
        Date startDate = previousContrac.startDate;
        Date today = Dates.today();
        return Dates.diffDays(today, startDate) / 365.;
    }
}
