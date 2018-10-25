package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ClientGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que contiene la persistencia para clientes.
 */
public class ClientGatewayImpl implements ClientGateway {

    /**
     * Metodo que recura un cliente.
     *
     * @param campo Campo por el que se quiere recuperar un cliente.
     * @param valor Valor del campo que se quiere encontrar.
     * @return Cliente que se ha obtenido.
     * @throws PersistanceException
     */
    @Override
    public ClientDto findClient(String campo, Long valor) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ClientDto client = new ClientDto();
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance()
                    .getProperty("SQL_FIND_CLIENTE_ID_BY_ID_FACTURA"));
            pst.setBigDecimal(1, BigDecimal.valueOf(valor));
            rs = pst.executeQuery();

            while (rs.next()) {
                client.id = rs.getLong("cliente_id");
            }

            return client;
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el cliente de la factura:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }
}
