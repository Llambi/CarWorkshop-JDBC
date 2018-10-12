package uo.ri.persistence;

import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public interface PaymentMeanGateway {

    List<PaymentMeanDto> findPaymentMean(String campo, Long valor) throws PersistanceException;
    void updatePaymentMean(PaymentMeanDto paymentMean);
    void updatePaymentMean(VoucherDto paymentMean);
}
