package uo.ri.business.impl.transactionScript.invoice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import alb.util.jdbc.Jdbc;
import uo.ri.business.InvoiceService;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.CashDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.conf.ServiceFactory;
import uo.ri.persistence.InvoiceGateway;
import uo.ri.persistence.PaymentMeanGateway;
import uo.ri.persistence.exception.PersistanceException;

/**
 * Clase que contiene la logica para liquidar una factura.
 */
public class SettleInvoice {

    private final InvoiceService forInvoice =
            new ServiceFactory().forInvoice();
    private final PaymentMeanGateway paymentMeanGateway =
            GatewayFactory.getPaymentMeanGateway();
    private final InvoiceGateway invoiceGateway =
            GatewayFactory.getInvoiceGateway();
    private Long idFactura;
    private Map<Long, Double> cargos;   //Metodo de pago al que cargar la cantidad dada
    private Connection connection;

    public SettleInvoice(Long idFactura, Map<Long, Double> cargos) {

        this.idFactura = idFactura;
        this.cargos = cargos;
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
            List<PaymentMeanDto> paymentMeanDtos = forInvoice
                    .findPayMethodsForInvoice(this.idFactura);

            faltaPorPagar = checkPaymentMeanSelected(GatewayFactory
                            .getInvoiceGateway().listInvoice(idFactura)
                    , paymentMeanDtos);
            if (faltaPorPagar <= 0) {
                liquidarFactura(paymentMeanDtos);
                faltaPorPagar = 0;
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException();
        } catch (PersistanceException e) {
            throw new BusinessException
                    ("La Factura solicitada no existe.");
        } finally {
            Jdbc.close(connection);
        }
        return faltaPorPagar;
    }

    /**
     * Metodo que comprueba los metodos de pago seleccionados para
     * liquidar una factura.
     *
     * @param invoice
     * @param paymentMeanDtos
     * @return Double con el importe que queda por pagar.
     * @throws BusinessException
     */
    private double checkPaymentMeanSelected
    (InvoiceDto invoice, List<PaymentMeanDto> paymentMeanDtos)
            throws BusinessException {
        double total = invoice.total;
        double cantidad;

        for (Long tipoPago : cargos.keySet()) {
            PaymentMeanDto paymentMeanDto =
                    findPaymentMean(paymentMeanDtos, tipoPago);
            cantidad = cargos.get(tipoPago);

            if (cantidad <= 0) {
                throw new BusinessException(
                        "La cantidad tiene que ser mayor que 0€");
            }
            if (Math.toIntExact(tipoPago) == 3) {
                if (cantidad > ((VoucherDto) paymentMeanDto).available) {
                    throw new BusinessException
                            ("La cantidad introducida para " +
                                    "este bono supera la disponible");
                }
            }
            total -= cantidad;
        }

        return total;
    }

    /**
     * Metodo que liquida la factura.
     *
     * @param paymentMeanDtos
     * @throws BusinessException
     */
    private void liquidarFactura(List<PaymentMeanDto> paymentMeanDtos)
            throws BusinessException {
        double cantidad;

        for (Long tipoPago : cargos.keySet()) {
            PaymentMeanDto paymentMeanDto =

                    findPaymentMean(paymentMeanDtos, tipoPago);
            cantidad = cargos.get(tipoPago);

            paymentMeanDto.accumulated += cantidad;

            if (Math.toIntExact(tipoPago) == 3) {
                ((VoucherDto) paymentMeanDto).available -= cantidad;
            }
            try {
                paymentMeanGateway.updatePaymentMean(paymentMeanDto);
            } catch (PersistanceException e) {
                throw new BusinessException
                        ("No se puede actualizar el metodo de pago.");
            }
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
            invoiceGateway.updateInvoice("status",
                    "ABONADA", this.idFactura);
        } catch (PersistanceException e) {
            throw new BusinessException("Factura no abonada:\n\t" + e);
        }

    }

    /**
     * Metodo que devuelve el metodo de pago del cliente que corresponde.
     *
     * @param type       Clave para seleccionar el medio de pago.
     * @param mediosPago
     * @return Medio de pago del cliente seleccionado.
     * @throws BusinessException
     */
    private PaymentMeanDto findPaymentMean
    (List<PaymentMeanDto> mediosPago, Long type)
            throws BusinessException {
        PaymentMeanDto paymentMean = null;
        switch (Math.toIntExact(type)) {
            case 1:
                paymentMean = findPaymentMeanCash(mediosPago);
                break;
            case 2:
                paymentMean = findPaymentMeanCard(mediosPago);
                break;
            case 3:
                paymentMean = findPaymentMeanVoucher(mediosPago);
                break;
        }
        return paymentMean;
    }

    /**
     * Metodo que devuelve el medio de pago de bono del cliente.
     *
     * @param mediosPago
     * @return Bono de un cliente.
     * @throws BusinessException
     */
    private VoucherDto findPaymentMeanVoucher
    (List<PaymentMeanDto> mediosPago) throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof VoucherDto) {
                return (VoucherDto) paymentMean;
            }
        }
        throw new BusinessException("El cliente no tiene Bonos para" +
                " usar como pago.");
    }

    /**
     * Metodo que devuelve el medio de pago de tarjeta del cliente.
     *
     * @param mediosPago
     * @return Tarjeta de un cliente.
     * @throws BusinessException
     */
    private CardDto findPaymentMeanCard(List<PaymentMeanDto> mediosPago)
            throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof CardDto) {
                return (CardDto) paymentMean;
            }
        }
        throw new BusinessException
                ("El cliente no tiene Tarjeta para usar como pago.");
    }

    /**
     * Metodo que devuelve el medio de pago de en metalico del cliente.
     *
     * @param mediosPago
     * @return Metalico de un cliente.
     * @throws BusinessException
     */
    private CashDto findPaymentMeanCash(List<PaymentMeanDto> mediosPago)
            throws BusinessException {
        for (PaymentMeanDto paymentMean : mediosPago) {
            if (paymentMean instanceof CashDto) {
                return (CashDto) paymentMean;
            }
        }
        throw new BusinessException
                ("El cliente no tiene Metalico para usar como pago.");
    }
}
