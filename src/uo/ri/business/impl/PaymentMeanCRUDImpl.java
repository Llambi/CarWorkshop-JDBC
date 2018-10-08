package uo.ri.business.impl;

import uo.ri.business.PaymentMeanCRUDService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.impl.paymentMean.FindClientPaymentMean;
import uo.ri.business.impl.paymentMean.updatePaymentMean;
import uo.ri.common.BusinessException;

import java.sql.Connection;
import java.util.List;

public class PaymentMeanCRUDImpl implements PaymentMeanCRUDService {
    @Override
    public List<PaymentMeanDto> findClientPaymentMean(InvoiceDto invoice) throws BusinessException {
        return new FindClientPaymentMean(invoice).execute();
    }

    @Override
    public void updatePaymentMean(Connection connection, PaymentMeanDto paymentMean) {
        new updatePaymentMean(connection, paymentMean).execute();
    }

    @Override
    public void updatePaymentMean(Connection connection, VoucherDto paymentMean) {
        new updatePaymentMean(connection, paymentMean).execute();
    }
}
