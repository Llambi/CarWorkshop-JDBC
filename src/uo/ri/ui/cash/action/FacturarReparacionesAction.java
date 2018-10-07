package uo.ri.ui.cash.action;

import java.util.LinkedList;
import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.InvoiceCRUDService;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.impl.InvoiceCRUDImpl;
import uo.ri.business.impl.invoice.CreateInvoice;
import uo.ri.common.BusinessException;
import uo.ri.conf.ServiceFactory;

public class FacturarReparacionesAction implements Action {

	@Override
	public void execute() throws BusinessException {
		List<Long> ids = new LinkedList<>();
		// pedir las averias a incluir en la factura
		do {
			Long id = Console.readLong("ID de averia");
			ids.add(id);
		} while ( masAverias() );

		InvoiceDto invoice = new ServiceFactory().getInvoiceCRUDService().createInvoice(ids);

		mostrarFactura(invoice);

	}

	private void mostrarFactura(InvoiceDto invoice) {
		
		Console.printf("Factura nº: %d\n", invoice.number);
		Console.printf("\tFecha: %1$td/%1$tm/%1$tY\n", invoice.date);
		Console.printf("\tTotal: %.2f €\n", invoice.amount);
		Console.printf("\tIva: %.1f %% \n", invoice.vat);
		Console.printf("\tTotal con IVA: %.2f €\n", invoice.total);
	}



	private boolean masAverias() {
		return Console.readString("¿Añadir más averias? (s/n) ").equalsIgnoreCase("s");
	}

}
