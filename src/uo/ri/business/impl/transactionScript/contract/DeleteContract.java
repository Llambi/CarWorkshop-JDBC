package uo.ri.business.impl.transactionScript.contract;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.exception.PersistanceException;

/**
 * Clase con contiene la logica para la eliminacion de un contrato
 */
public class DeleteContract {
    private long id;
    private Connection connection;
    private ContractGateway contractGateway =
            GatewayFactory.getContractGateway();
    private BreakdownGateway breakdownGateway =
            GatewayFactory.getBreakdownGateway();
    private ContractDto contractDto;

    public DeleteContract(Long id) {
        this.id = id;
    }

    /**
     * Metodo que comprueba si el nuevo contrato cumple los
     * prerequisitos y tras ello lo elimina.
     *
     * @throws BusinessException
     */
    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            //Recuperamos contrato
            contractDto = contractGateway.findContractById(this.id);
            if(contractDto==null)
                throw new BusinessException("No existe el contrato.");
            if (!checkMechanicActivity()) {
                contractGateway.deleteContract(contractDto);
            } else {
                throw new BusinessException
                        ("No se cumple lo requerido para " +
                                "eliminar el contrato.");
            }

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException
                        ("Imposible eliminar el contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback");
            }
        } finally {
            Jdbc.close(connection);
        }
    }

    /**
     * Metodo que comprueba si se ha trabajado durante un contrato.
     *
     * @return True si un mecanico a realizado, al menos, una
     * reparacion o tiene nominas ese mes y False si no.
     * @throws BusinessException
     */
    private boolean checkMechanicActivity() throws BusinessException {
        List<BreakdownDto> breakDowns = null;
        int payroolCount = 0;
        try {

            breakDowns = breakdownGateway
                    .findBreakDownsByMechanicId(contractDto.mechanicId);
            payroolCount = GatewayFactory.getPayrollGateway()
                    .countPayRolls(this.contractDto);

        } catch (PersistanceException e) {
            throw new BusinessException
                    ("Imposible comprobar la actividad " +
                            "de un mecanico.\n\t" + e);
        }

        int activityInYear = Math.toIntExact(breakDowns.stream()
                .filter(breakdownDto ->
                        Dates.isAfter(breakdownDto.date, contractDto.startDate)
                                || Dates.isBefore(breakdownDto.date,
                                contractDto.endDate))
                .count());

        return activityInYear > 0 || payroolCount > 0;
    }
}
