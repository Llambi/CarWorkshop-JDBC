package uo.ri.business.impl.transactionScript.invoice;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;

public class ListInvoice {

    private Connection connection;
    private Long number;

    public ListInvoice(Long number) {
        this.number = number;
    }

    public InvoiceDto execute() throws BusinessException {
        InvoiceDto invoice = null;

        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            invoice = verificarFacturaNoAbonada(this.number);

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

    private InvoiceDto verificarFacturaNoAbonada(Long number) throws BusinessException {

        try {
            InvoiceDto invoice = GatewayFactory.getInvoiceGateway().listInvoice(number);

            if (!"ABONADA".equalsIgnoreCase(invoice.status)) {
                throw new BusinessException("No está abonada la factura con numero: " + invoice.number);
            }

            return invoice;

        } catch (PersistanceException e) {
            throw new BusinessException("Error al recuperara la factura:"+e.getStackTrace());
        }

    }
}
