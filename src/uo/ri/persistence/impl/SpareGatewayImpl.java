package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.conf.Conf;
import uo.ri.persistence.SpareGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que contiene la persistencia de los repuestos.
 */
public class SpareGatewayImpl implements SpareGateway {
    /**
     * Metodo que recupera el importe total de un repuesto.
     *
     * @param idBreakdown Identificador del respuesto del que se quiere saber su importe.
     * @return Double con el importe total de un repuesto.
     * @throws PersistanceException
     */
    @Override
    public Double getSpareTotalImport(Long idBreakdown) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_IMPORTE_REPUESTOS"));
            pst.setLong(1, idBreakdown);

            rs = pst.executeQuery();
            if (!rs.next()) {
                return 0.0; // La averia puede no tener repuestos
            }

            return rs.getDouble(1);

        } catch (SQLException e) {
            throw new PersistanceException("Error en el calculo del importe total de repuestos:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }
}
