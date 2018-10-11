package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BreakdownGatewayImpl implements BreakdownGateway {
    @Override
    public BreakdownDto findBreakdown(long id) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        BreakdownDto breakdown;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_FIND_AVERIA"));


            pst.setLong(1, id);

            rs = pst.executeQuery();
            if (!rs.next()) {
                throw new PersistanceException("No existe la averia " + id);
            }

            //ID DESC FECHA IMPORT STATUS FACTURA MECANI VEHI
            breakdown = new BreakdownDto();
            breakdown.id = rs.getInt(1);
            breakdown.description = rs.getString(2);
            breakdown.date = rs.getDate(3);
            breakdown.total = rs.getDouble(4);
            breakdown.status = rs.getString(5);
            breakdown.invoiceId = rs.getLong(6);
            // El 7 no interesa
            breakdown.vehicleId = rs.getInt(8);


        } catch (SQLException e) {
            throw new PersistanceException("Erro base de datos:\n\t" + e.getStackTrace());
        } finally {
            Jdbc.close(rs, pst);
        }
        return breakdown;
    }

    @Override
    public void updateBreakdown(Long id,String column, String status) throws PersistanceException {
        PreparedStatement pst = null;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_ACTUALIZAR_ESTADO_AVERIA_GENERICO"));

            pst.setString(1, column);
            pst.setString(2, status);
            pst.setLong(3, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("No se ha actualizado la averia: \n\t" + e.getStackTrace());
        } finally {
            Jdbc.close(pst);
        }
    }

    @Override
    public void updateBreakdown(Long id, String column, Long status) throws PersistanceException {
        PreparedStatement pst = null;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_ACTUALIZAR_ESTADO_AVERIA_GENERICO"));

            pst.setString(1, column);
            pst.setLong(2, status);
            pst.setLong(3, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("No se ha actualizado la averia: \n\t" + e.getStackTrace());
        } finally {
            Jdbc.close(pst);
        }
    }

    @Override
    public void updateBreakdown(Long id, String column, Double status) throws PersistanceException {
        PreparedStatement pst = null;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_ACTUALIZAR_ESTADO_AVERIA_GENERICO"));

            pst.setString(1, column);
            pst.setDouble(2, status);
            pst.setLong(3, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("No se ha actualizado la averia: \n\t" + e.getStackTrace());
        } finally {
            Jdbc.close(pst);
        }
    }

}
