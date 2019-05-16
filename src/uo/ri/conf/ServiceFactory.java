package uo.ri.conf;

import uo.ri.business.*;
import uo.ri.business.impl.ContractCRUDImpl;
import uo.ri.business.impl.ContractCategoryCRUDImpl;
import uo.ri.business.impl.ContractTypeCRUDImpl;
import uo.ri.business.impl.InvoiceCRUDImpl;

public class ServiceFactory implements uo.ri.business.ServiceFactory {
//TODO:
    @Override
    public MechanicCrudService forMechanicCrudService() {
        return null;
        //return new MechanicCRUDImpl();
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
        return null;
        //return new PayrollGatewayImpl();
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
