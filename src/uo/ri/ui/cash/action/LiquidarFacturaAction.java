package uo.ri.ui.cash.action;

import alb.util.console.Console;
import alb.util.date.Dates;
import alb.util.menu.Action;
import uo.ri.business.InvoiceCRUDService;
import uo.ri.business.dto.*;
import uo.ri.business.impl.invoice.ListInvoice;
import uo.ri.common.BusinessException;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.List;

public class LiquidarFacturaAction implements Action {

    /**
     * Proceso:
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
        Long id = Console.readLong("Numero de la factura:");

        InvoiceCRUDService invoiceService = new ServiceFactory().getInvoiceCRUDService();

        InvoiceDto invoice = invoiceService.ListInvoice(id);
        mostrarFactura(invoice);

        List<PaymentMeanDto> paymentMeans = invoiceService.findClientPaymentMean(invoice);
        mostrarPaymentMean(paymentMeans);

        payInvoice(paymentMeans, invoice);

    }

    private void payInvoice(List<PaymentMeanDto> mediosPago, InvoiceDto invoice) throws BusinessException {
        //TODO: Queda por hacer la parte de actualizar el cobro de las facturas
        double total, restante, pagado = 0;
        String eleccion, cantidad;

        total = invoice.total;

        Printer.printRecaudarMediosPago("Total", total);
        do {
            Printer.printRecaudarMediosPago("Restante", total - pagado);
            eleccion = Console.readString("Selecciona el numero del medio de"
                    + " pago que desea utilizar");
            cantidad = Console.readString(
                    "Selecciona la cantidad que desea pagar con este medio");
            formatoPagos.put(eleccion, cantidad);
            restante = cashService.checkTotalFactura(factura, formatoPagos,
                    mediosPago);
            pagado = total - restante;
        } while (restante != 0);
    }

    private void mostrarFactura(InvoiceDto invoice) {

        Console.printf("Factura nº: %d\n", invoice.number);
        Console.printf("\tFecha: %1$td/%1$tm/%1$tY\n", invoice.date);
        Console.printf("\tTotal: %.2f €\n", invoice.amount);
        Console.printf("\tIva: %.1f %% \n", invoice.vat);
        Console.printf("\tTotal con IVA: %.2f €\n", invoice.total);
    }

    private void mostrarPaymentMean(List<PaymentMeanDto> paymentMeans) {
        Console.printf("Medios de pago para el cliente %s:\n", paymentMeans.get(1).clientId);
        for (PaymentMeanDto payment : paymentMeans) {
            Console.printf("\tNumero de metodo de pago: %d || Cpn una cantidad acumulada de %d", payment.id, payment.accumulated);
            mostrarPaymentMean(payment);
        }
    }

    private void mostrarPaymentMean(PaymentMeanDto payment) {

        if (payment instanceof CardDto) {
            CardDto card = (CardDto) payment;
            Console.printf("\tNumero de tarjeta: %s || Validez hasta: %s || Tipo de tarjeta: %s", card.cardNumber, Dates.toString(card.cardExpiration), card.cardType);
        } else if (payment instanceof VoucherDto) {
            VoucherDto voucher = (VoucherDto) payment;
            Console.printf("\tCodigo de bono: %s || Validez: %d || Descripcion: %s", voucher.code, voucher.available, voucher.description);
        }
    }

}
