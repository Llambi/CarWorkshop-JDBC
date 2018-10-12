package uo.ri.persistence.impl;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.InvoiceGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class InvoiceGatewayImpl implements InvoiceGateway {
    @Override
    public InvoiceDto createInvoice(InvoiceDto invoice) throws PersistanceException {
        PreparedStatement pst = null;

        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_INSERTAR_FACTURA"));
            pst.setLong(1, invoice.number);
            pst.setDate(2, new java.sql.Date(invoice.date.getTime()));
            pst.setDouble(3, invoice.vat);
            pst.setDouble(4, invoice.total);
            pst.setString(5, "SIN_ABONAR");
            invoice.status ="SIN_ABONAR";

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Factura no insertada:\n\t"+e.getStackTrace());
        } finally {
            Jdbc.close(pst);
        }
        return invoice;
    }

    @Override
    public List<BreakdownDto> readInvoice(Long id) {
        return null;
    }

    @Override
    public InvoiceDto ListInvoice(Long number) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        InvoiceDto resultInvoice = new InvoiceDto();
        try {

            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_INVOICE"));
            pst.setLong(1, number);

            rs = pst.executeQuery();

            if (!rs.next()) {
                throw new PersistanceException("No existe la factura con numero: " + number);
            }

            resultInvoice.id = rs.getLong(1);
            resultInvoice.date = Dates.fromString(rs.getString(2));
            resultInvoice.total = rs.getLong(3);
            resultInvoice.vat = rs.getLong(4);
            resultInvoice.number = rs.getLong(5);
            resultInvoice.status = rs.getString(6);

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar la factura:"+e.getStackTrace());
        } finally {
            Jdbc.close(rs, pst);
        }
        return resultInvoice;
    }

    @Override
    public Long ListLastInvoice() throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_ULTIMO_NUMERO_FACTURA"));
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getLong(1) + 1; // +1, el siguiente
            } else {  // todavía no hay ninguna
                return 1L;
            }
        } catch (SQLException e) {
            throw new PersistanceException("Problemas al recuperar la ultima factura:\n\t"+e.getStackTrace());
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public double checkTotalInvoice(InvoiceDto invoice, Map<Integer, PaymentMeanDto> formatoPagos, List<PaymentMeanDto> mediosPago) {
        return 0;
    }
}
