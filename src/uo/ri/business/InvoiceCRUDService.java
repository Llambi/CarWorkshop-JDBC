package uo.ri.business;

import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.common.BusinessException;

import java.util.List;

public interface InvoiceCRUDService {

    InvoiceDto createInvoice(List<Long> ids) throws BusinessException;

    List<BreakdownDto> readInvoice(Long id);

    InvoiceDto ListInvoice(Long number) throws BusinessException;

    List<PaymentMeanDto> findClientPaymentMean(InvoiceDto invoice) throws BusinessException;

}
