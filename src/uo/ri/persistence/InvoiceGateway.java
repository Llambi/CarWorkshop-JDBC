package uo.ri.persistence;

import uo.ri.business.dto.InvoiceDto;
import uo.ri.persistence.exception.PersistanceException;

public interface InvoiceGateway {
    InvoiceDto createInvoice(InvoiceDto invoice) throws PersistanceException;

    InvoiceDto listInvoice(Long number) throws PersistanceException;

    Long listLastInvoice() throws PersistanceException;

    void updateInvoice(String campo, String estado, Long id) throws PersistanceException;

}
