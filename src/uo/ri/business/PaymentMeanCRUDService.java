package uo.ri.business;

import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.exception.BusinessException;

import java.sql.Connection;
import java.util.List;

public interface PaymentMeanCRUDService {

    List<PaymentMeanDto> findClientPaymentMean(InvoiceDto invoice) throws BusinessException;

    void updatePaymentMean(PaymentMeanDto paymentMean) throws BusinessException;

    void updatePaymentMean(VoucherDto paymentMean) throws BusinessException;
}
