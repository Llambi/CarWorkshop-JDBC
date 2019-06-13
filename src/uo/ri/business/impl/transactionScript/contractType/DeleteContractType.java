package uo.ri.business.impl.transactionScript.contractType;

import alb.util.jdbc.Jdbc;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.ContractTypeGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

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

            if (contractTypeGateway.findContractTypeById(id) == null)
                throw new BusinessException("El tipo de contrato no existe.");
            long contractDtos = contractGateway.findContractByTypeId(id).stream().
                    filter(contractDto -> contractDto.status.equalsIgnoreCase(ACTIVE.toString())).count();

            if (contractDtos > 0)
                throw new BusinessException("No se cumple lo requerido para elimianr el tipo de contrato.");

            contractTypeGateway.deleteContractType(id);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible eliminar el tipo de contrato.\n\t" + e.getMessage());
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }
}
