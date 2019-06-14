package uo.ri.persistence;

import java.util.List;

import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.persistence.exception.PersistanceException;

public interface PaymentMeanGateway {

    List<PaymentMeanDto> findPaymentMean(String campo, Long valor) throws PersistanceException;
    void updatePaymentMean(PaymentMeanDto paymentMean) throws PersistanceException;
    void updatePaymentMean(VoucherDto paymentMean) throws PersistanceException;
}
