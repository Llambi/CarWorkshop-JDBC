package uo.ri.persistence;

import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;
import java.util.Map;

public interface InvoiceGateway {
    InvoiceDto createInvoice(InvoiceDto invoice) throws PersistanceException;

    List<BreakdownDto> readInvoice(Long id);

    InvoiceDto ListInvoice(Long number) throws PersistanceException;

    Long ListLastInvoice() throws PersistanceException;

    double checkTotalInvoice(InvoiceDto invoice, Map<Integer, PaymentMeanDto> formatoPagos, List<PaymentMeanDto> mediosPago);

}
