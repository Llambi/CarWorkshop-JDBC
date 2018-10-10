package uo.ri.business.impl.transactionScript.invoice;

import alb.util.jdbc.Jdbc;
import uo.ri.business.PaymentMeanCRUDService;
import uo.ri.business.dto.*;
import uo.ri.business.impl.PaymentMeanCRUDImpl;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.Conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UpdateInvoice {

    private InvoiceDto invoice;
    private Map<Integer, PaymentMeanDto> pagosSeleccionados;
    private List<PaymentMeanDto> mediosPago;
    private Connection connection;

    public UpdateInvoice(InvoiceDto invoice, Map<Integer, PaymentMeanDto> pagosSeleccionados, List<PaymentMeanDto> mediosPago) {
        this.invoice = invoice;
        this.pagosSeleccionados = pagosSeleccionados;
        this.mediosPago = mediosPago;
    }

    public double execute() throws BusinessException {
        double faltaPorPagar;
        try {
            this.connection = Jdbc.getConnection();
            this.connection.setAutoCommit(false);

            faltaPorPagar = checkPaymentMeanSelected();
            if (faltaPorPagar <= 0) {
                liquidarFactura();
                faltaPorPagar = 0;
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException();
        } finally {
            Jdbc.close(connection);
        }
        return faltaPorPagar;
    }

    private double checkPaymentMeanSelected() throws BusinessException {
        double total = invoice.total;
        Integer type;
        double cantidad;
        for (Map.Entry<Integer, PaymentMeanDto> paymentMeanTuple : pagosSeleccionados.entrySet()) {
            type = paymentMeanTuple.getKey();
            cantidad = paymentMeanTuple.getValue().accumulated;

            if (cantidad <= 0) {
                throw new BusinessException(
                        "La cantidad tiene que ser mayor que 0€");
            }
            if (type == 3) {
                if (cantidad > findPaymentMeanVoucher().available) {
                    throw new BusinessException("La cantidad introducida para este bono supera la disponible");
                }
            }
            total -= cantidad;
        }

        return total;

    }

    private void liquidarFactura() throws BusinessException {
        Integer type;
        double cantidad;
        PaymentMeanCRUDService invoiceService = new PaymentMeanCRUDImpl();

        for (Map.Entry<Integer, PaymentMeanDto> paymentMeanTuple : pagosSeleccionados.entrySet()) {
            type = paymentMeanTuple.getKey();
            cantidad = paymentMeanTuple.getValue().accumulated;
            PaymentMeanDto paymentMeanSelected = findPaymentMean(type);

            paymentMeanSelected.accumulated += cantidad;

            if (paymentMeanSelected instanceof VoucherDto) {
                ((VoucherDto) paymentMeanSelected).available -= cantidad;
            }

            invoiceService.updatePaymentMean(connection, paymentMeanSelected);
        }

        updateInvoiceAbonada();
    }

    private void updateInvoiceAbonada() {
        PreparedStatement pst = null;

        try {
            pst = connection
                    .prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_INVOICE_ABONADA"));
            pst.setString(1, "ABONADA");
            pst.setLong(2, invoice.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(pst);
        }
    }

    private PaymentMeanDto findPaymentMean(Integer type) throws BusinessException {
        PaymentMeanDto paymentMean = null;
        switch (type) {
            case 1:
                paymentMean = findPaymentMeanCash();
                break;
            case 2:
                paymentMean = findPaymentMeanCard();
                break;
            case 3:
                paymentMean = findPaymentMeanCard();
                break;
        }
        return paymentMean;
    }

    private VoucherDto findPaymentMeanVoucher() throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof VoucherDto) {
                return (VoucherDto) paymentMean;
            }
        }
        throw new BusinessException("El cliente no tiene Bonos para usar como pago.");
    }

    private CardDto findPaymentMeanCard() throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof CardDto) {
                return (CardDto) paymentMean;
            }
        }
        throw new BusinessException("El cliente no tiene Tarjeta para usar como pago.");
    }

    private CashDto findPaymentMeanCash() throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof CashDto) {
                return (CashDto) paymentMean;
            }
        }
        throw new BusinessException("El cliente no tiene Metalico para usar como pago.");
    }
}
