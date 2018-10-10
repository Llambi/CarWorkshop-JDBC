package uo.ri.conf;

import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.InvoiceGateway;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.impl.BreakdownGatewayImpl;
import uo.ri.persistence.impl.InvoiceGatewayImpl;
import uo.ri.persistence.impl.MechanicGatewayImpl;

public class GatewayFactory {

    public static MechanicGateway getMechanicGateway() { return new MechanicGatewayImpl();}
    public static InvoiceGateway getInvoiceGateway() { return new InvoiceGatewayImpl();}
    public static BreakdownGateway getBreakdownGateway() { return new BreakdownGatewayImpl();}
}
