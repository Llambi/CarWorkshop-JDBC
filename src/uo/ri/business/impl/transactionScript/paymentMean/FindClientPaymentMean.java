package uo.ri.business.impl.transactionScript.paymentMean;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FindClientPaymentMean {

    private Connection connection;
    private InvoiceDto invoice;

    public FindClientPaymentMean(InvoiceDto invoice) {
        this.invoice = invoice;
    }

    public List<PaymentMeanDto> execute() throws BusinessException {
        Long idCliente;
        List<PaymentMeanDto> paymentMeans;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            idCliente = findClientByFactura().id;
            paymentMeans = findMediosPagoCliente(idCliente);

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(connection);
        }
        return paymentMeans;
    }

    private ClientDto findClientByFactura() throws BusinessException {

        try {
            ClientDto cliente = GatewayFactory.getClientGateway().findClient("FACTURA_ID", invoice.id);
            if (cliente.id == 0) {
                throw new BusinessException(
                        "El cliente no se encuentra en el sistema");
            }
            return cliente;
        } catch (PersistanceException e) {
            throw new BusinessException("Error al recuperar el cliente:\n\t" + e);
        }

    }

    private List<PaymentMeanDto> findMediosPagoCliente(Long idCliente) throws BusinessException {

        try {
            return GatewayFactory.getPaymentMeanGateway().findPaymentMean("cliente_id", idCliente);
        } catch (PersistanceException e) {
            throw new BusinessException("Error al recuperar los medios de pago del cliente:\n\t" + e);
        }

    }
}
