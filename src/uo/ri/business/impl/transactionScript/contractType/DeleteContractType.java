package uo.ri.business.impl.transactionScript.contractType;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.ContractTypeGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static uo.ri.persistence.impl.ContracStatus.ACTIVE;

/**
 * Clase que contiene la logica para la eliminacion de un tipo de contrato
 */
public class DeleteContractType {
    private final ContractGateway contractGateway = GatewayFactory.getContractGateway();
    private final ContractTypeGateway contractTypeGateway = GatewayFactory.getContractTypeGateway();
    private Connection connection;
    private Long id;

    public DeleteContractType(Long id) {
        this.id = id;
    }

    /**
     * Metodo que comprueba los prerequisitos de la eliminacion de un tipo de contrato y si los cumple lo elimina.
     *
     * @throws BusinessException
     */
    public void execute() throws BusinessException {

        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<ContractDto> contractDtos = contractGateway.findContractByTypeId(id);
            for (ContractDto contractDto : contractDtos) {
                if (!contractDto.status.equals(ACTIVE.toString())) {
                    contractDtos.remove(contractDto);
                }
            }
            if (contractDtos.size() == 0) {
                contractTypeGateway.deleteContractType(id);
            } else {
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
