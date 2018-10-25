package uo.ri.business.impl.transactionScript.paymentMean;

import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

/**
 * Clase que contiene la logica para actualizar los medios de pago.
 */
public class UpdatePaymentMean {

    private PaymentMeanDto paymentMean;

    public UpdatePaymentMean(PaymentMeanDto paymentMean) {
        this.paymentMean = paymentMean;
    }

    /**
     * Metodo que actuliza un medio de pago dado.
     *
     * @throws BusinessException
     */
    public void execute() throws BusinessException {
        try {
            if (paymentMean instanceof VoucherDto) {
                updateVoucher();
            } else {
                updatePayment();
            }
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible actualizar los medios de pago.\n\t" + e);
        }

    }

    /**
     * Metodo que actuliza un medio de pago, ya sea en metalico o tarjeta.
     *
     * @throws PersistanceException
     */
    private void updatePayment() throws PersistanceException {
        GatewayFactory.getPaymentMeanGateway().updatePaymentMean(paymentMean);
    }

    /**
     * Metodo que actuliza un medio de pago de tipo Bono.
     *
     * @throws PersistanceException
     */
    private void updateVoucher() throws PersistanceException {
        GatewayFactory.getPaymentMeanGateway().updatePaymentMean((VoucherDto) paymentMean);
    }
}
