package uo.ri.conf;

import uo.ri.business.InvoiceCRUDService;
import uo.ri.business.MechanicCRUDService;
import uo.ri.business.PaymentMeanCRUDService;
import uo.ri.business.impl.InvoiceCRUDImpl;
import uo.ri.business.impl.MechanicCRUDImpl;
import uo.ri.business.impl.PaymentMeanCRUDImpl;

public class ServiceFactory {

    public MechanicCRUDService getMechanicCRUDService() {
        return new MechanicCRUDImpl();
    }

    public InvoiceCRUDService getInvoiceCRUDService() {
        return new InvoiceCRUDImpl();
    }

    public PaymentMeanCRUDService getPaymentMeanCRUDService(){
        return new PaymentMeanCRUDImpl();
    }
}
