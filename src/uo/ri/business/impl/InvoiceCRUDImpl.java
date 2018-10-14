package uo.ri.business.impl;

import uo.ri.business.InvoiceCRUDService;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.impl.transactionScript.invoice.CreateInvoice;
import uo.ri.business.impl.transactionScript.invoice.ListInvoice;
import uo.ri.business.impl.transactionScript.invoice.ReadInvoice;
import uo.ri.business.impl.transactionScript.invoice.UpdateInvoice;
import uo.ri.business.exception.BusinessException;

import java.util.List;
import java.util.Map;

public class InvoiceCRUDImpl implements InvoiceCRUDService {
    @Override
    public InvoiceDto createInvoice(List<Long> ids) throws BusinessException {
        return new CreateInvoice(ids).execute();
    }

    @Override
    public List<BreakdownDto> readInvoice(Long id) throws BusinessException {
        return new ReadInvoice(id).execute();
    }

    @Override
    public InvoiceDto ListInvoice(Long number) throws BusinessException {
        return new ListInvoice(number).execute();
    }

    @Override
    public double checkTotalInvoice(InvoiceDto invoice, Map<Integer, PaymentMeanDto> formatoPagos, List<PaymentMeanDto> mediosPago) throws BusinessException {
        return new UpdateInvoice(invoice, formatoPagos, mediosPago).execute();
    }


}
