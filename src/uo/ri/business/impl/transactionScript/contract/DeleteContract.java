package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DeleteContract {
    private ContractDto contractDto;
    private Connection connection
            ;

    public DeleteContract(ContractDto contractDto) {
        this.contractDto = contractDto;
    }

    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            //Recuperamos contrato
            contractDto = GatewayFactory.getContractGateway().findContract(contractDto);
            if(contractDto.id==null){
                throw new BusinessException("No se cumple lo requerido para eliminar el contrato.");
            }
            if(!checkMechanicActivity()){
                GatewayFactory.getContractGateway().deleteContract(contractDto);
            }else{
                throw new BusinessException("No se cumple lo requerido para eliminar el contrato.");
            }

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible eliminar el contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback");
            }
        } finally {
            Jdbc.close(connection);
        }
    }

    private boolean checkMechanicActivity() throws BusinessException {
        List<BreakdownDto> breakDowns = null;
        try {
            breakDowns = GatewayFactory.getBreakdownGateway().findMechanicBreakDowns(contractDto);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible comprobar la actividad de un mecanico.\n\t" + e);
        }
        int activityInYear=0;
        for (BreakdownDto breakdownDto: breakDowns) {
            if (Dates.isAfter(breakdownDto.date, contractDto.startDate) && Dates.isBefore(breakdownDto.date, contractDto.endDate)) {
                activityInYear++;
            }
        }
        return activityInYear>0;
    }
}
