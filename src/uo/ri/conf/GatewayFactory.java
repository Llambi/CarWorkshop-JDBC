package uo.ri.conf;

import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.ClientGateway;
import uo.ri.persistence.ContractCategoryGateway;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.ContractTypeGateway;
import uo.ri.persistence.InterventionGateway;
import uo.ri.persistence.InvoiceGateway;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.PaymentMeanGateway;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.SpareGateway;
import uo.ri.persistence.impl.BreakdownGatewayImpl;
import uo.ri.persistence.impl.ClientGatewayImpl;
import uo.ri.persistence.impl.ContractCategoryGatewayImpl;
import uo.ri.persistence.impl.ContractGatewayImpl;
import uo.ri.persistence.impl.ContractTypeGatewayImpl;
import uo.ri.persistence.impl.InterventionGatewayImpl;
import uo.ri.persistence.impl.InvoiceGatewayImpl;
import uo.ri.persistence.impl.MechanicGatewayImpl;
import uo.ri.persistence.impl.PaymentMeanGatewayImpl;
import uo.ri.persistence.impl.PayrollGatewayImpl;
import uo.ri.persistence.impl.SpareGatewayImpl;

public class GatewayFactory {

    public static MechanicGateway getMechanicGateway() {
        return new MechanicGatewayImpl();
    }

    public static InvoiceGateway getInvoiceGateway() {
        return new InvoiceGatewayImpl();
    }

    public static BreakdownGateway getBreakdownGateway() {
        return new BreakdownGatewayImpl();
    }

    public static SpareGateway getSpareGateway() {
        return new SpareGatewayImpl();
    }

    ;

    public static InterventionGateway getInterventionGateway() {
        return new InterventionGatewayImpl();
    }

    public static ClientGateway getClientGateway() {
        return new ClientGatewayImpl();
    }

    public static PaymentMeanGateway getPaymentMeanGateway() {
        return new PaymentMeanGatewayImpl();
    }

    public static ContractTypeGateway getContractTypeGateway() {
        return new ContractTypeGatewayImpl();
    }

    public static PayrollGateway getPayrollGateway() {
        return new PayrollGatewayImpl();
    }

    public static ContractGateway getContractGateway() {
        return new ContractGatewayImpl();
    }

    public static ContractCategoryGateway getContractCategoryGateway() {
        return new ContractCategoryGatewayImpl();
    }
}
