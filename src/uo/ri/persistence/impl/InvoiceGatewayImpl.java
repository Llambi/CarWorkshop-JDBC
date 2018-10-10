package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.InvoiceGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class InvoiceGatewayImpl implements InvoiceGateway {
    @Override
    public InvoiceDto createInvoice(InvoiceDto invoice) throws BusinessException, PersistanceException {
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
    public InvoiceDto ListInvoice(Long number) throws BusinessException {
        return null;
    }

    @Override
    public double checkTotalInvoice(InvoiceDto invoice, Map<Integer, PaymentMeanDto> formatoPagos, List<PaymentMeanDto> mediosPago) throws BusinessException {
        return 0;
    }
}
