package uo.ri.business.invoice;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.math.Round;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.common.BusinessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CreateInvoice {
    private static final String SQL_IMPORTE_REPUESTOS =
            "select sum(s.cantidad * r.precio) " +
                    "	from  TSustituciones s, TRepuestos r " +
                    "	where s.repuesto_id = r.id " +
                    "		and s.intervencion_averia_id = ?";

    private static final String SQL_IMPORTE_MANO_OBRA =
            "select sum(i.minutos * tv.precioHora / 60) " +
                    "	from TAverias a, TIntervenciones i, TVehiculos v, TTiposVehiculo tv" +
                    "	where i.averia_id = a.id " +
                    "		and a.vehiculo_id = v.id" +
                    "		and v.tipo_id = tv.id" +
                    "		and a.id = ?" +
                    "		and a.status = 'TERMINADA'";

    private static final String SQL_UPDATE_IMPORTE_AVERIA =
            "update TAverias set importe = ? where id = ?";

    private static final String SQL_ULTIMO_NUMERO_FACTURA =
            "select max(numero) from TFacturas";

    private static final String SQL_INSERTAR_FACTURA =
            "insert into TFacturas(numero, fecha, iva, importe, status) " +
                    "	values(?, ?, ?, ?, ?)";

    private static final String SQL_VINCULAR_AVERIA_FACTURA =
            "update TAverias set factura_id = ? where id = ?";

    private static final String SQL_ACTUALIZAR_ESTADO_AVERIA =
            "update TAverias set status = ? where id = ?";

    private static final String SQL_VERIFICAR_ESTADO_AVERIA =
            "select status from TAverias where id = ?";

    private static final String SQL_RECUPERAR_CLAVE_GENERADA =
            "select id from TFacturas where numero = ?";
    private LinkedList<BreakdownDto> breakdowns;
    private Connection connection;

    public CreateInvoice(LinkedList<BreakdownDto> Breakdown) {
        this.breakdowns = Breakdown;
    }

    public InvoiceDto execute() throws BusinessException {
        InvoiceDto invoice = new InvoiceDto();

        try {
            connection = Jdbc.getConnection();
            connection.setAutoCommit(false);

            verificarAveriasTerminadas(breakdowns);


            invoice.number = generarNuevoNumeroFactura();
            invoice.date = Dates.today();
            invoice.amount = calcularImportesAverias(breakdowns);
            invoice.vat = porcentajeIva(invoice.date);
            double importe = invoice.amount * (1 + invoice.vat / 100);
            invoice.total = Round.twoCents(importe);

            invoice.id = crearFactura(invoice);
            vincularAveriasConFactura(invoice.id, breakdowns);
            cambiarEstadoAverias(breakdowns);

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

    private void verificarAveriasTerminadas(List<BreakdownDto> breakdowns) throws SQLException, BusinessException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(SQL_VERIFICAR_ESTADO_AVERIA);

            for (BreakdownDto breakdown : breakdowns) {
                pst.setLong(1, breakdown.id);

                rs = pst.executeQuery();
                if (!rs.next()) {
                    throw new BusinessException("No existe la averia " + breakdown.id);
                }

                String status = rs.getString(1);
                if (!"TERMINADA".equalsIgnoreCase(status)) {
                    throw new BusinessException("No está terminada la avería " + breakdown.id);
                }

                rs.close();
            }
        } finally {
            Jdbc.close(rs, pst);
        }

    }

    private void cambiarEstadoAverias(List<BreakdownDto> breakdowns) throws SQLException {

        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(SQL_ACTUALIZAR_ESTADO_AVERIA);

            for (BreakdownDto breakdown : breakdowns) {
                pst.setString(1, "FACTURADA");
                pst.setLong(2, breakdown.id);

                pst.executeUpdate();
            }
        } finally {
            Jdbc.close(pst);
        }
    }

    private void vincularAveriasConFactura(long idFactura, List<BreakdownDto> breakdowns) throws SQLException {

        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(SQL_VINCULAR_AVERIA_FACTURA);

            for (BreakdownDto breakdown : breakdowns) {
                pst.setLong(1, idFactura);
                pst.setLong(2, breakdown.id);

                pst.executeUpdate();
            }
        } finally {
            Jdbc.close(pst);
        }
    }

    private long crearFactura(InvoiceDto invoice) throws SQLException {

        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(SQL_INSERTAR_FACTURA);
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
            pst = connection.prepareStatement(SQL_RECUPERAR_CLAVE_GENERADA);
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
            pst = connection.prepareStatement(SQL_ULTIMO_NUMERO_FACTURA);
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

    private double calcularImportesAverias(List<BreakdownDto> breakdowns)
            throws BusinessException, SQLException {

        double totalFactura = 0.0;
        for (BreakdownDto breakdown : breakdowns) {
            double importeManoObra = consultaImporteManoObra(breakdown.id);
            double importeRepuestos = consultaImporteRepuestos(breakdown.id);
            double totalAveria = importeManoObra + importeRepuestos;

            actualizarImporteAveria(breakdown.id, totalAveria);

            totalFactura += totalAveria;
        }
        return totalFactura;
    }

    private void actualizarImporteAveria(Long idAveria, double totalAveria) throws SQLException {
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(SQL_UPDATE_IMPORTE_AVERIA);
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
            pst = connection.prepareStatement(SQL_IMPORTE_REPUESTOS);
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
            pst = connection.prepareStatement(SQL_IMPORTE_MANO_OBRA);
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
