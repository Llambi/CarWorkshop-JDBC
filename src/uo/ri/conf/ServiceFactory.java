package uo.ri.conf;

import uo.ri.business.CloseBreakdownService;
import uo.ri.business.ContractCategoryCrudService;
import uo.ri.business.ContractCrudService;
import uo.ri.business.ContractTypeCrudService;
import uo.ri.business.InvoiceService;
import uo.ri.business.MechanicCrudService;
import uo.ri.business.PayrollService;
import uo.ri.business.VehicleReceptionService;
import uo.ri.business.impl.ContractCRUDImpl;
import uo.ri.business.impl.ContractCategoryCRUDImpl;
import uo.ri.business.impl.ContractTypeCRUDImpl;
import uo.ri.business.impl.InvoiceCRUDImpl;
import uo.ri.business.impl.MechanicCRUDImpl;
import uo.ri.business.impl.PayrollCRUDImpl;

public class ServiceFactory implements uo.ri.business.ServiceFactory {

    @Override
    public  MechanicCrudService forMechanicCrudService() {
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
