package uo.ri.business.impl.transactionScript.paymentMean;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.*;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.Conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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
            connection = Jdbc.getConnection();
            connection.setAutoCommit(false);

            idCliente = findClientByFactura();
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

    private Long findClientByFactura() throws BusinessException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        long idCliente = 0L;
        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CLIENTE_ID_BY_ID_FACTURA"));
            pst.setLong(1, invoice.id);
            rs = pst.executeQuery();

            while (rs.next()) {
                idCliente = rs.getLong("cliente_id");
            }

            if (idCliente == 0) {
                throw new BusinessException(
                        "El cliente no se encuentra en el sistema");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Jdbc.close(rs, pst);
        }
        return idCliente;
    }

    private List<PaymentMeanDto> findMediosPagoCliente(Long idCliente) throws BusinessException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<PaymentMeanDto> paymentMeans = new LinkedList<>();
        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_ALL_MEDIOS_PAGO"));
            pst.setLong(1, idCliente);
            rs = pst.executeQuery();

            while (rs.next()) {
                PaymentMeanDto paymentMean;
                switch (rs.getString("dtype")) {
                    case "TMetalico":
                        CashDto metalico = new CashDto();
                        metalico.id = rs.getLong("id");
                        metalico.accumulated = rs.getDouble("acumulado");
                        metalico.clientId = rs.getLong("cliente_id");
                        paymentMean = metalico;
                        break;
                    case "TBonos":
                        VoucherDto bono = new VoucherDto();
                        bono.id = rs.getLong("id");
                        bono.accumulated = rs.getDouble("acumulado");
                        bono.clientId = rs.getLong("cliente_id");
                        bono.code = rs.getString("codigo");
                        bono.available = rs.getDouble("disponible");
                        bono.description = rs.getString("descripcion");
                        paymentMean = bono;
                        break;
                    case "TTarjetasCredito":
                        CardDto tarjeta = new CardDto();
                        tarjeta.id = rs.getLong("id");
                        tarjeta.accumulated = rs.getDouble("acumulado");
                        tarjeta.clientId = rs.getLong("cliente_id");
                        tarjeta.cardNumber = rs.getString("numero");
                        tarjeta.cardType = rs.getString("tipo");
                        tarjeta.cardExpiration = rs.getTimestamp("validez");
                        paymentMean = tarjeta;
                        break;
                    default:
                        throw new BusinessException("No existe el metodo de pago: " + rs.getString("dtype"));
                }
                paymentMeans.add(paymentMean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Jdbc.close(rs, pst);
        }
        return paymentMeans;
    }
}
