package uo.ri.ui.cash.action;

import alb.util.console.Console;
import alb.util.date.Dates;
import alb.util.menu.Action;
import uo.ri.business.InvoiceService;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiquidarFacturaAction implements Action {

    private InvoiceService invoiceService;

    public LiquidarFacturaAction() {
        this.invoiceService = new ServiceFactory().forInvoice();
    }

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

        Long id = Console.readLong("Numero de la factura");

        InvoiceDto invoice = invoiceService.findInvoice(id);
        mostrarFactura(invoice);
        List<PaymentMeanDto> paymentMeans = invoiceService.findPayMethodsForInvoice(id);
        mostrarPaymentMean(paymentMeans);

        payInvoice(paymentMeans, invoice);

    }

    private void payInvoice(List<PaymentMeanDto> mediosPago, InvoiceDto invoice) throws BusinessException {

        double total, restante, pagado = 0;
        Long eleccion;
        Double cantidad;
        Map<Long, Double> pagosSeleccionados = new HashMap<>();
        total = invoice.total;

        Printer.printRecaudarMediosPago("Total", total);

        do {
            Printer.printRecaudarMediosPago("Restante", total - pagado);
            eleccion = Console.readLong("Selecciona el numero del medio de"
                    + " pago que desea utilizar:\n\t1) Metalico\n\t2) Tarjeta\n\t3) Bono\nIndice  seleccionado");
            cantidad = Console.readDouble(
                    "Selecciona la cantidad que desea pagar con este medio");
            if (pagosSeleccionados.containsKey(eleccion)) {
                Double oldAmount = pagosSeleccionados.get(eleccion);
                pagosSeleccionados.replace(eleccion, oldAmount + cantidad);
            } else {
                pagosSeleccionados.put(eleccion, cantidad);
            }
            invoiceService.settleInvoice(invoice.id, pagosSeleccionados);
            pagado += cantidad;
            restante = total - pagado;
        } while (restante != 0);
        Printer.printLiquidarFactura();
    }

    private void mostrarFactura(InvoiceDto invoice) {

        Console.printf("Factura nº: %d\n", invoice.number);
        Console.printf("\tFecha: %1$td/%1$tm/%1$tY\n", invoice.date);
        Console.printf("\tTotal: %.2f €\n", invoice.amount);
        Console.printf("\tIva: %.1f %% \n", invoice.vat);
        Console.printf("\tTotal con IVA: %.2f €\n\n", invoice.total);
    }

    private void mostrarPaymentMean(List<PaymentMeanDto> paymentMeans) {
        Console.printf("Medios de pago para el cliente %s:\n\n", paymentMeans.get(1).clientId);
        for (PaymentMeanDto payment : paymentMeans) {
            Console.printf("\t> Numero de metodo de pago: %s || Con una cantidad acumulada de %s\n", payment.id, Math.round(payment.accumulated * 100) / 100);
            mostrarPaymentMean(payment);
        }
        System.out.println();
    }

    private void mostrarPaymentMean(PaymentMeanDto payment) {

        if (payment instanceof CardDto) {
            CardDto card = (CardDto) payment;
            Console.printf("\t\tTipo tarjeta, numero: %s || Validez hasta: %s || Tipo de tarjeta: %s\n\n", card.cardNumber, Dates.toString(card.cardExpiration), card.cardType);
        } else if (payment instanceof VoucherDto) {
            VoucherDto voucher = (VoucherDto) payment;
            Console.printf("\t\tTipo bono, numero: %s || Validez: %d || Descripcion: %s\n\n", voucher.code, voucher.available, voucher.description);
        } else {
            Console.println("\t\tTipo efectivo.\n");
        }
    }

}
