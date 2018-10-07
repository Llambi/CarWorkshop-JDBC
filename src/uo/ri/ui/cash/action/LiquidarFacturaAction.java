package uo.ri.ui.cash.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.impl.invoice.UpdateInvoice;
import uo.ri.common.BusinessException;
import uo.ri.conf.ServiceFactory;

public class LiquidarFacturaAction implements Action {

    /**
     * Proceso:
     * <p>
     * - Pedir al usuario el nº de factura
     * - Recuperar los datos de la factura
     * - Mostrar la factura en pantalla
     * - Verificar que está sin abonar (status <> 'ABONADA')
     * - Listar en pantalla los medios de pago registrados para el cliente
     * - Mostrar los medios de pago
     * - Pedir el importe a cargar en cada medio de pago.
     * -- Verificar que la suma de los cargos es igual al importe de la factura
     * - Registrar los cargos en la BDD
     * - Incrementar el acumulado de cada medio de pago
     * - Si se han empleado bonos, decrementar el saldo disponible
     * - Finalmente, marcar la factura como abonada
     */
    @Override
    public void execute() throws BusinessException {
        //TODO: Queda parte de la liquidacion de una factura.
        InvoiceDto invoice = new InvoiceDto();
        invoice.number = Console.readLong("Numero de la factura:");

        UpdateInvoice updateInvoice = new UpdateInvoice(invoice);
        //invoice = new ServiceFactory().getInvoiceCRUDService().updateInvoice.execute();

        mostrarFactura(invoice);
    }

    private void mostrarFactura(InvoiceDto invoice) {

        Console.printf("Factura nº: %d\n", invoice.number);
        Console.printf("\tFecha: %1$td/%1$tm/%1$tY\n", invoice.date);
        Console.printf("\tTotal: %.2f €\n", invoice.amount);
        Console.printf("\tIva: %.1f %% \n", invoice.vat);
        Console.printf("\tTotal con IVA: %.2f €\n", invoice.total);
    }

}
