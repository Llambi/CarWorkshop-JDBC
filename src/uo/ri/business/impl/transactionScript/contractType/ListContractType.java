package uo.ri.business.impl.transactionScript.contractType;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase que contiene la logica para el listado de los tipos de contrato con los mecanicos que lo tienen y el acumulado
 * del salrio de estos.
 */
public class ListContractType {
    private Connection connection;

    /**
     * Metodo que recupera los tipos de contrato existentes j
     *
     * @return Una lista con la informacion antes detallada.
     * @throws BusinessException
     */
    public List<ContractTypeDto> execute() throws BusinessException {
        List<ContractTypeDto> contractTypes;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);


            contractTypes = GatewayFactory.getContractTypeGateway().findAllContractTypes();


            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible recuperar los tipos de contratos.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
        return contractTypes;
    }
}
