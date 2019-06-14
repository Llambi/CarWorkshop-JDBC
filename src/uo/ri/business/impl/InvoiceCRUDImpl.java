package uo.ri.business.impl;

import java.util.List;
import java.util.Map;

import uo.ri.business.InvoiceService;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.invoice.CreateInvoice;
import uo.ri.business.impl.transactionScript.invoice.FindPayMethodsForInvoice;
import uo.ri.business.impl.transactionScript.invoice.ListInvoice;
import uo.ri.business.impl.transactionScript.invoice.ReadInvoice;
import uo.ri.business.impl.transactionScript.invoice.SettleInvoice;

public class InvoiceCRUDImpl implements InvoiceService {

    @Override
    public InvoiceDto createInvoiceFor(List<Long> idsAveria)
            throws BusinessException {
        return new CreateInvoice(idsAveria).execute();
    }

    @Override
    public InvoiceDto findInvoice(Long numeroFactura)
            throws BusinessException {
        return new ListInvoice(numeroFactura).execute();
    }

    @Override
    public List<PaymentMeanDto> findPayMethodsForInvoice(Long idFactura)
            throws BusinessException {
        return new FindPayMethodsForInvoice(idFactura).execute();
    }

    @Override
    public void settleInvoice(Long idFactura, Map<Long, Double> cargos)
            throws BusinessException {
        new SettleInvoice(idFactura, cargos).execute();
    }

    @Override
    public List<BreakdownDto> findRepairsByClient(String dni)
            throws BusinessException {
        return new ReadInvoice(dni).execute();
    }

}
