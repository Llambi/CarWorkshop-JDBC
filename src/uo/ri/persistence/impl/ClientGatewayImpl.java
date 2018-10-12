package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ClientGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientGatewayImpl implements ClientGateway {

    @Override
    public ClientDto findClient(String campo, Long valor) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ClientDto client = new ClientDto();

        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CLIENTE_GENERICO"));
            pst.setString(1, campo);
            pst.setLong(2, valor);
            rs = pst.executeQuery();

            while (rs.next()) {
                client.id = rs.getLong("cliente_id");
            }

            return client;
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el cliente de la factura:\n\t" + e.getStackTrace());
        } finally {
            Jdbc.close(rs, pst);
        }
    }
}
