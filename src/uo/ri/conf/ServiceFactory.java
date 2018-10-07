package uo.ri.conf;

import uo.ri.business.InvoiceCRUDService;
import uo.ri.business.MechanicCRUDService;
import uo.ri.business.impl.InvoiceCRUDImpl;
import uo.ri.business.impl.MechanicCRUDImpl;

public class ServiceFactory {

    public MechanicCRUDService getMechanicCRUDService() {
        return new MechanicCRUDImpl();
    }

    public InvoiceCRUDService getInvoiceCRUDService() {
        return new InvoiceCRUDImpl();
    }
}
