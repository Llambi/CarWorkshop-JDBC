package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase que contiene la logica para la actualizacion de un contrato
 */
public class UpdateContract {
    private ContractDto contractDto;
    private Connection connection;

    public UpdateContract(ContractDto contractDto) {
        this.contractDto = contractDto;
        this.contractDto.endDate = Dates.lastDayOfMonth(this.contractDto.endDate);
    }


    /**
     * Metodo que comprueba los prerequisitos para las actualizacion de un contrato y si lo hace los lleva a cabo.
     *
     * @throws BusinessException
     */
    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            if (Dates.isBefore(contractDto.endDate, Dates.today()) || contractDto.yearBaseSalary < 0.) {
                throw new BusinessException("No se cumple lo requerido para actualizar el contrato.");
            }
            GatewayFactory.getContractGateway().updateContract(contractDto);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible actualizar el contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }
}
