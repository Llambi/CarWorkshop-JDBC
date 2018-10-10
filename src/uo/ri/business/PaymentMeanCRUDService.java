package uo.ri.business;

import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.exception.BusinessException;

import java.sql.Connection;
import java.util.List;

public interface PaymentMeanCRUDService {

    List<PaymentMeanDto> findClientPaymentMean(InvoiceDto invoice) throws BusinessException;

    void updatePaymentMean(Connection connection, PaymentMeanDto paymentMean);

    void updatePaymentMean(Connection connection, VoucherDto paymentMean);
}
