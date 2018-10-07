package uo.ri.business.impl.invoice;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateInvoice {

    private Connection connection;
    private InvoiceDto invoice;

    public UpdateInvoice(InvoiceDto invoice) {
        this.invoice = invoice;
    }

    public InvoiceDto execute() throws BusinessException {
        //TODO: Queda parte de la liquidacion de una factura.
        InvoiceDto invoice = null;

        try {
            connection = Jdbc.getConnection();
            connection.setAutoCommit(false);

            invoice = verificarFacturaNoAbonada(this.invoice);

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(e);
            }
        } finally {
            Jdbc.close(connection);
        }

        return invoice;
    }

    private InvoiceDto verificarFacturaNoAbonada(InvoiceDto invoice) throws SQLException, BusinessException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        InvoiceDto resultInvoice;
        try {

            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_INVOICE"));
            pst.setLong(1, invoice.number);

            rs = pst.executeQuery();

            if (!rs.next()) {
                throw new BusinessException("No existe la factura con numero: " + invoice.number);
            }

            resultInvoice = new InvoiceDto();
            resultInvoice.id = rs.getLong(1);
            resultInvoice.date = Dates.fromString(rs.getString(2));
            resultInvoice.total = rs.getLong(3);
            resultInvoice.vat = rs.getLong(4);
            resultInvoice.number = rs.getLong(5);
            resultInvoice.status = rs.getString(6);

            if (!"ABONADA".equalsIgnoreCase(resultInvoice.status)) {
                throw new BusinessException("No está abonada la factura con numero: " + resultInvoice.number);
            }

        } finally {
            Jdbc.close(rs, pst);
        }
        return resultInvoice;
    }
}
