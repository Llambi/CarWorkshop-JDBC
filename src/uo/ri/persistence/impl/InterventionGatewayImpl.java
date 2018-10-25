package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.conf.Conf;
import uo.ri.persistence.InterventionGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que contiene la persistencia de las intervenciones.
 */
public class InterventionGatewayImpl implements InterventionGateway {
    /**
     * Metodo que recupera el coste de mano de obra de una intervencion.
     *
     * @param idBreakdown Identificador de la averia de la intervencion.
     * @return Double con el importe de la mano de obra.
     * @throws PersistanceException
     */
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
