package uo.ri.conf;

import uo.ri.persistence.*;
import uo.ri.persistence.impl.*;

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
}
