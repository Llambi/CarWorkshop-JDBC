package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.conf.Conf;
import uo.ri.persistence.InterventionGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InterventionGatewayImpl implements InterventionGateway {
    @Override
    public Double getManPowerTotalImport(Long idBreakdown) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_IMPORTE_MANO_OBRA"));
            pst.setLong(1, idBreakdown);

            rs = pst.executeQuery();
            if (!rs.next()) {
                throw new PersistanceException("La averia no existe o no se puede facturar");
            }

            return rs.getDouble(1);

        } catch (SQLException e) {
            throw new PersistanceException("Error en el calculo del importe de la mano de obra:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }
}
