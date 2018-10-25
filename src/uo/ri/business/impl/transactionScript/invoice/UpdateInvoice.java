package uo.ri.business.impl.transactionScript.invoice;

import alb.util.jdbc.Jdbc;
import uo.ri.business.PaymentMeanCRUDService;
import uo.ri.business.dto.*;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.PaymentMeanCRUDImpl;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Clase que contiene la logica para liquidar una factura.
 */
public class UpdateInvoice {

    private InvoiceDto invoice;
    private Map<Integer, PaymentMeanDto> pagosSeleccionados;
    private List<PaymentMeanDto> mediosPago;
    private Connection connection;

    public UpdateInvoice(InvoiceDto invoice, Map<Integer, PaymentMeanDto> pagosSeleccionados
            , List<PaymentMeanDto> mediosPago) {
        this.invoice = invoice;
        this.pagosSeleccionados = pagosSeleccionados;
        this.mediosPago = mediosPago;
    }

    /**
     * Metodo que realiza las operaciones necesarias para liquidar un factura dada.
     *
     * @return
     * @throws BusinessException
     */
    public double execute() throws BusinessException {
        double faltaPorPagar;
        try {
            this.connection = Jdbc.createThreadConnection();
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

    /**
     * Metodo que comprueba los metodos de pago seleccionados para liquidar una factura.
     *
     * @return Double con el importe que queda por pagar.
     * @throws BusinessException
     */
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

    /**
     * Metodo que liquida la factura.
     *
     * @throws BusinessException
     */
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

            invoiceService.updatePaymentMean(paymentMeanSelected);
        }

        updateInvoiceAbonada();
    }

    /**
     * Metodo que actualiza una factura al estado ABONADA.
     *
     * @throws BusinessException
     */
    private void updateInvoiceAbonada() throws BusinessException {

        try {
            GatewayFactory.getInvoiceGateway().updateInvoice("status", "ABONADA", invoice.id);
        } catch (PersistanceException e) {
            throw new BusinessException("Factura no abonada:\n\t" + e);
        }

    }

    /**
     * Metodo que devuelve el metodo de pago del cliente que corresponde.
     *
     * @param type Clave para seleccionar el medio de pago.
     * @return Medio de pago del cliente seleccionado.
     * @throws BusinessException
     */
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

    /**
     * Metodo que devuelve el medio de pago de bono del cliente.
     *
     * @return Bono de un cliente.
     * @throws BusinessException
     */
    private VoucherDto findPaymentMeanVoucher() throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof VoucherDto) {
                return (VoucherDto) paymentMean;
            }
        }
        throw new BusinessException("El cliente no tiene Bonos para usar como pago.");
    }

    /**
     * Metodo que devuelve el medio de pago de tarjeta del cliente.
     *
     * @return Tarjeta de un cliente.
     * @throws BusinessException
     */
    private CardDto findPaymentMeanCard() throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof CardDto) {
                return (CardDto) paymentMean;
            }
        }
        throw new BusinessException("El cliente no tiene Tarjeta para usar como pago.");
    }

    /**
     * Metodo que devuelve el medio de pago de en metalico del cliente.
     *
     * @return Metalico de un cliente.
     * @throws BusinessException
     */
    private CashDto findPaymentMeanCash() throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof CashDto) {
                return (CashDto) paymentMean;
            }
        }
        throw new BusinessException("El cliente no tiene Metalico para usar como pago.");
    }
}
