package uo.ri.business.impl;

import uo.ri.business.InvoiceCRUDService;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.impl.invoice.CreateInvoice;
import uo.ri.business.impl.invoice.ReadInvoice;
import uo.ri.common.BusinessException;

import java.util.List;

public class InvoiceCRUDImpl implements InvoiceCRUDService {
    @Override
    public InvoiceDto createInvoice(List<Long> ids) throws BusinessException {
        return new CreateInvoice(ids).execute();
    }

    @Override
    public List<BreakdownDto> readInvoice(Long id) {
        return new ReadInvoice(id).execute();
    }
}
