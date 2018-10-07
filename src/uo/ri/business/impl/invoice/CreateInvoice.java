package uo.ri.business.impl.invoice;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.math.Round;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CreateInvoice {

    private List<Long> ids;
    private Connection connection;

    public CreateInvoice(List<Long> ids) {
        this.ids = ids;
    }

    public InvoiceDto execute() throws BusinessException {
        InvoiceDto invoice = new InvoiceDto();

        try {
            connection = Jdbc.getConnection();
            connection.setAutoCommit(false);

            verificarAveriasTerminadas(ids);


            invoice.number = generarNuevoNumeroFactura();
            invoice.date = Dates.today();
            invoice.amount = calcularImportesAverias(ids);
            invoice.vat = porcentajeIva(invoice.date);
            double importe = invoice.amount * (1 + invoice.vat / 100);
            invoice.total = Round.twoCents(importe);

            invoice.id = crearFactura(invoice);
            vincularAveriasConFactura(invoice.id, ids);
            cambiarEstadoAverias(ids);

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException(e);
        } catch (BusinessException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw e;
        } finally {
            Jdbc.close(connection);
        }
        return invoice;
    }

    private void verificarAveriasTerminadas(List<Long> ids) throws SQLException, BusinessException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_VERIFICAR_ESTADO_AVERIA"));

            for (Long id : ids) {
                pst.setLong(1, id);

                rs = pst.executeQuery();
                if (!rs.next()) {
                    throw new BusinessException("No existe la averia " + id);
                }

                String status = rs.getString(1);
                if (!"TERMINADA".equalsIgnoreCase(status)) {
                    throw new BusinessException("No está terminada la avería " + id);
                }

                rs.close();
            }
        } finally {
            Jdbc.close(rs, pst);
        }

    }

    private void cambiarEstadoAverias(List<Long> ids) throws SQLException {

        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_ACTUALIZAR_ESTADO_AVERIA"));

            for (Long id : ids) {
                pst.setString(1, "FACTURADA");
                pst.setLong(2, id);

                pst.executeUpdate();
            }
        } finally {
            Jdbc.close(pst);
        }
    }

    private void vincularAveriasConFactura(long idFactura, List<Long> ids) throws SQLException {

        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_VINCULAR_AVERIA_FACTURA"));

            for (Long id : ids) {
                pst.setLong(1, idFactura);
                pst.setLong(2, id);

                pst.executeUpdate();
            }
        } finally {
            Jdbc.close(pst);
        }
    }

    private long crearFactura(InvoiceDto invoice) throws SQLException {

        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_INSERTAR_FACTURA"));
            pst.setLong(1, invoice.number);
            pst.setDate(2, new java.sql.Date(invoice.date.getTime()));
            pst.setDouble(3, invoice.vat);
            pst.setDouble(4, invoice.total);
            pst.setString(5, "SIN_ABONAR");

            pst.executeUpdate();

            return getGeneratedKey(invoice.number); // Id de la nueva factura generada

        } finally {
            Jdbc.close(pst);
        }
    }

    private long getGeneratedKey(long numeroFactura) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_RECUPERAR_CLAVE_GENERADA"));
            pst.setLong(1, numeroFactura);
            rs = pst.executeQuery();
            rs.next();

            return rs.getLong(1);

        } finally {
            Jdbc.close(rs, pst);
        }
    }

    private Long generarNuevoNumeroFactura() throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_ULTIMO_NUMERO_FACTURA"));
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getLong(1) + 1; // +1, el siguiente
            } else {  // todavía no hay ninguna
                return 1L;
            }
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    private double porcentajeIva(Date fechaFactura) {
        return Dates.fromString("1/7/2012").before(fechaFactura) ? 21.0 : 18.0;
    }

    private double calcularImportesAverias(List<Long> ids)
            throws BusinessException, SQLException {

        double totalFactura = 0.0;
        for (Long id : ids) {
            double importeManoObra = consultaImporteManoObra(id);
            double importeRepuestos = consultaImporteRepuestos(id);
            double totalAveria = importeManoObra + importeRepuestos;

            actualizarImporteAveria(id, totalAveria);

            totalFactura += totalAveria;
        }
        return totalFactura;
    }

    private void actualizarImporteAveria(Long idAveria, double totalAveria) throws SQLException {
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_IMPORTE_AVERIA"));
            pst.setDouble(1, totalAveria);
            pst.setLong(2, idAveria);
            pst.executeUpdate();
        } finally {
            Jdbc.close(pst);
        }
    }

    private double consultaImporteRepuestos(Long idAveria) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_IMPORTE_REPUESTOS"));
            pst.setLong(1, idAveria);

            rs = pst.executeQuery();
            if (!rs.next()) {
                return 0.0; // La averia puede no tener repuestos
            }

            return rs.getDouble(1);

        } finally {
            Jdbc.close(rs, pst);
        }
    }

    private double consultaImporteManoObra(Long idAveria) throws BusinessException, SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_IMPORTE_MANO_OBRA"));
            pst.setLong(1, idAveria);

            rs = pst.executeQuery();
            if (!rs.next()) {
                throw new BusinessException("La averia no existe o no se puede facturar");
            }

            return rs.getDouble(1);

        } finally {
            Jdbc.close(rs, pst);
        }

    }
}
