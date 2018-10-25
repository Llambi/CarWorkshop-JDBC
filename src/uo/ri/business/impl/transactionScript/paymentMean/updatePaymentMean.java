package uo.ri.business.impl.transactionScript.paymentMean;

import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

public class UpdatePaymentMean {

    private PaymentMeanDto paymentMean;

    public UpdatePaymentMean(PaymentMeanDto paymentMean) {
        this.paymentMean = paymentMean;
    }

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

    private void updatePayment() throws PersistanceException {
        GatewayFactory.getPaymentMeanGateway().updatePaymentMean(paymentMean);
    }

    private void updateVoucher() throws PersistanceException {
        GatewayFactory.getPaymentMeanGateway().updatePaymentMean((VoucherDto) paymentMean);
    }
}
