package uo.ri.conf;

import uo.ri.business.*;
import uo.ri.business.impl.*;
import uo.ri.persistence.impl.PayrollGatewayImpl;

public class ServiceFactory implements uo.ri.business.ServiceFactory {
//TODO:
    @Override
    public MechanicCrudService forMechanicCrudService() {
        return new MechanicCRUDImpl();
    }

    @Override
    public ContractCrudService forContractCrud() {
        return new ContractCRUDImpl();
    }

    @Override
    public ContractTypeCrudService forContractTypeCrud() {
        return new ContractTypeCRUDImpl();
    }

    @Override
    public ContractCategoryCrudService forContractCategoryCrud() {
        return new ContractCategoryCRUDImpl();
    }

    @Override
    public PayrollService forPayroll() {
        return new PayrollCRUDImpl();
    }

    @Override
    public InvoiceService forInvoice() {
        return new InvoiceCRUDImpl();
    }

    @Override
    public VehicleReceptionService forVehicleReception() {
        return null;
        // return new VehicleReceptionImpl();
    }

    @Override
    public CloseBreakdownService forClosingBreakdown() {
        return null;
        //return new CloseBreakdownImpl();
    }
}
