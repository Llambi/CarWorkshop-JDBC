package uo.ri.business.impl.transactionScript.contractType;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;
import uo.ri.persistence.impl.ContracStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static uo.ri.persistence.impl.ContracStatus.*;

public class DeleteContractType {
    private ContractTypeDto contractTypeDto;
    private Connection connection;

    public DeleteContractType(ContractTypeDto contractTypeDto) {
        this.contractTypeDto = contractTypeDto;
    }

    public void execute() throws BusinessException {

        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<ContractDto> contractDtos = GatewayFactory.getContractGateway().findContract(contractTypeDto);
            for (ContractDto contractDto: contractDtos){
                if(contractDto.status!= ACTIVE.toString()){
                    contractDtos.remove(contractDto);
                }
            }
            if(contractDtos.size()==0) {
                GatewayFactory.getContractTypeGateway().deleteContractType(contractTypeDto);
            }else{
                throw new BusinessException("No se cumple lo requerido para elimianr el tipo de contrato.");
            }

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible eliminar el tipo de contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }
}
