package uo.ri.business.impl;

import uo.ri.business.PaymentMeanCRUDService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.impl.transactionScript.paymentMean.FindClientPaymentMean;
import uo.ri.business.impl.transactionScript.paymentMean.updatePaymentMean;
import uo.ri.business.exception.BusinessException;

import java.sql.Connection;
import java.util.List;

public class PaymentMeanCRUDImpl implements PaymentMeanCRUDService {
    @Override
    public List<PaymentMeanDto> findClientPaymentMean(InvoiceDto invoice) throws BusinessException {
        return new FindClientPaymentMean(invoice).execute();
    }

    @Override
    public void updatePaymentMean(PaymentMeanDto paymentMean) {
        new updatePaymentMean(paymentMean).execute();
    }

    @Override
    public void updatePaymentMean(VoucherDto paymentMean) {
        new updatePaymentMean(paymentMean).execute();
    }
}
