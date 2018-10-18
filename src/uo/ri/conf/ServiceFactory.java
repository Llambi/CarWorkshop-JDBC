package uo.ri.conf;

import uo.ri.business.ContractTypeCRUDService;
import uo.ri.business.InvoiceCRUDService;
import uo.ri.business.MechanicCRUDService;
import uo.ri.business.PaymentMeanCRUDService;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.impl.ContractTypeCRUDImpl;
import uo.ri.business.impl.InvoiceCRUDImpl;
import uo.ri.business.impl.MechanicCRUDImpl;
import uo.ri.business.impl.PaymentMeanCRUDImpl;

public class ServiceFactory {

    public static MechanicCRUDService getMechanicCRUDService() {
        return new MechanicCRUDImpl();
    }

    public static InvoiceCRUDService getInvoiceCRUDService() {
        return new InvoiceCRUDImpl();
    }

    public static PaymentMeanCRUDService getPaymentMeanCRUDService(){
        return new PaymentMeanCRUDImpl();
    }

    public static ContractTypeCRUDService getContractTypeCRUDService() { return new ContractTypeCRUDImpl(); }
}
