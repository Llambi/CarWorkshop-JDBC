package uo.ri.business.impl.transactionScript.contractType;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractTypeGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase que contiene la logica para la creacion de un tipo de contrato.
 */
public class AddContractType {
    private final ContractTypeGateway contractTypeGateway = GatewayFactory.getContractTypeGateway();
    private ContractTypeDto contractTypeDto;
    private Connection connection;

    public AddContractType(ContractTypeDto contractTypeDto) {

        this.contractTypeDto = contractTypeDto;
    }

    /**
     * Metodo que crea un nuevo tipo de contrato si es posible.
     *
     * @throws BusinessException
     */
    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            checkData();

            contractTypeGateway.addContractType(contractTypeDto);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Error al añadir el tipo de contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }

    }

    private void checkData() throws BusinessException, PersistanceException {
        if (contractTypeDto.compensationDays < 0)
            throw new BusinessException("El numero de dias de compensacion debe ser mayor que 0.");
        if (contractTypeGateway.findContractTypeByName(contractTypeDto.name) != null)
            throw new BusinessException("Ya existe un tipo de contrato con ese nombre.");
    }
}
