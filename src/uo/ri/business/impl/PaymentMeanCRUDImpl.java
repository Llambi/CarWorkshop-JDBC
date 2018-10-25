package uo.ri.business.impl;

import uo.ri.business.PaymentMeanCRUDService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.impl.transactionScript.paymentMean.FindClientPaymentMean;
import uo.ri.business.impl.transactionScript.paymentMean.UpdatePaymentMean;
import uo.ri.business.exception.BusinessException;

import java.util.List;

public class PaymentMeanCRUDImpl implements PaymentMeanCRUDService {
    @Override
    public List<PaymentMeanDto> findClientPaymentMean(InvoiceDto invoice) throws BusinessException {
        return new FindClientPaymentMean(invoice).execute();
    }

    @Override
    public void updatePaymentMean(PaymentMeanDto paymentMean) throws BusinessException {
        new UpdatePaymentMean(paymentMean).execute();
    }

    @Override
    public void updatePaymentMean(VoucherDto paymentMean) throws BusinessException {
        new UpdatePaymentMean(paymentMean).execute();
    }
}
