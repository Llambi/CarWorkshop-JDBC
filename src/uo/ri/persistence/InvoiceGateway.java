package uo.ri.persistence;

import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;
import java.util.Map;

public interface InvoiceGateway {
    InvoiceDto createInvoice(InvoiceDto invoice) throws BusinessException, PersistanceException;

    List<BreakdownDto> readInvoice(Long id);

    InvoiceDto ListInvoice(Long number) throws BusinessException;

    double checkTotalInvoice(InvoiceDto invoice, Map<Integer, PaymentMeanDto> formatoPagos, List<PaymentMeanDto> mediosPago) throws BusinessException;

}
