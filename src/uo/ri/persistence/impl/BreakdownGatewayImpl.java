package uo.ri.persistence.impl;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase contiene la persistencia de las averias.
 */
public class BreakdownGatewayImpl implements BreakdownGateway {
    /**
     * Metodo que encuentra la averia con un identificador
     *
     * @param id Identificador de la averia a obtener
     * @return Averia obtenida
     * @throws PersistanceException
     */
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
            throw new PersistanceException("Erro base de datos:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return breakdown;
    }

    /**
     * Metodo que actualiza una averia.
     *
     * @param id     Identificador de la averia.
     * @param column Campo que se quiere actualizar.
     * @param status Nuevo estado del campo anterior.
     * @throws PersistanceException
     */
    @Override
    public void updateBreakdown(Long id, String column, String status) throws PersistanceException {
        PreparedStatement pst = null;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance()
                    .getProperty("SQL_ACTUALIZAR_ESTADO_AVERIA_GENERICO"));

            pst.setString(1, column);
            pst.setString(2, status);
            pst.setLong(3, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("No se ha actualizado la averia: \n\t" + e);
        } finally {
            Jdbc.close(pst);
        }
    }

    /**
     * Metodo que actualiza una averia.
     *
     * @param id     Identificador de la averia.
     * @param column Campo que se quiere actualizar.
     * @param status Nuevo estado del campo anterior.
     * @throws PersistanceException
     */
    @Override
    public void updateBreakdown(Long id, String column, Long status) throws PersistanceException {
        PreparedStatement pst = null;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance()
                    .getProperty("SQL_ACTUALIZAR_ESTADO_AVERIA_GENERICO"));

            pst.setString(1, column);
            pst.setLong(2, status);
            pst.setLong(3, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("No se ha actualizado la averia: \n\t" + e);
        } finally {
            Jdbc.close(pst);
        }
    }

    /**
     * Metodo que actualiza una averia.
     *
     * @param id     Identificador de la averia.
     * @param column Campo que se quiere actualizar.
     * @param status Nuevo estado del campo anterior.
     * @throws PersistanceException
     */
    @Override
    public void updateBreakdown(Long id, String column, Double status) throws PersistanceException {
        PreparedStatement pst = null;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance()
                    .getProperty("SQL_ACTUALIZAR_ESTADO_AVERIA_GENERICO"));

            pst.setString(1, column);
            pst.setDouble(2, status);
            pst.setLong(3, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("No se ha actualizado la averia: \n\t" + e);
        } finally {
            Jdbc.close(pst);
        }
    }

    /**
     * Metodo que recupera una lista de averias dado el identificador de un cliente.
     *
     * @param id Identificador del cliente.
     * @return Lista de averias sin facturar
     * @throws PersistanceException
     */
    @Override
    public List<BreakdownDto> findUninvoicedBreakdown(Long id) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance()
                    .getProperty("SQL_AVERIAS_NO_FACTURADAS_CLIENTE"));

            pst.setLong(1, id);

            rs = pst.executeQuery();

            List<BreakdownDto> breakdowns = new LinkedList<>();
            while (rs.next()) {
                BreakdownDto breakdown = new BreakdownDto();
                breakdown.id = rs.getLong(1);
                breakdown.date = Dates.fromString(rs.getString(2));
                breakdown.status = rs.getString(3);
                breakdown.total = Double.parseDouble(rs.getString(4));
                breakdown.description = rs.getString(5);

                breakdowns.add(breakdown);
            }

            return breakdowns;

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar averias sin facturar:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    /**
     * Metodo que obtiene las averias realizadas por un mecanico.
     *
     * @param contractDto Contrato que contiene el id del mecanico del que se quieren obtener las averias.
     * @return Lista de averias del mecanico.
     * @throws PersistanceException
     */
    @Override
    public List<BreakdownDto> findMechanicBreakDowns(ContractDto contractDto) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<BreakdownDto> breakdowns = new LinkedList<>();
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_AVERIAS_MECANICO"));

            pst.setLong(1, contractDto.mechanicId);

            rs = pst.executeQuery();

            while (rs.next()) {
                BreakdownDto breakdown = new BreakdownDto();
                breakdown.id = rs.getLong(1);
                breakdown.date = Dates.fromString(rs.getString(2));
                breakdown.status = rs.getString(3);
                breakdown.total = rs.getDouble(4);
                breakdown.description = rs.getString(5);

                breakdowns.add(breakdown);
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar averias de un mecanico dado:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return breakdowns;
    }

}
