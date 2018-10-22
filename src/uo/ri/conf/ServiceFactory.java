package uo.ri.conf;

import uo.ri.business.*;
import uo.ri.business.impl.*;

public class ServiceFactory {

    public static MechanicCRUDService getMechanicCRUDService() {
        return new MechanicCRUDImpl();
    }

    public static InvoiceCRUDService getInvoiceCRUDService() {
        return new InvoiceCRUDImpl();
    }

    public static PaymentMeanCRUDService getPaymentMeanCRUDService() {
        return new PaymentMeanCRUDImpl();
    }

    public static ContractTypeCRUDService getContractTypeCRUDService() {
        return new ContractTypeCRUDImpl();
    }

    public static ContractCRUDService getContractCRUDService() {
        return new ContractCRUDImpl();
    }
}
