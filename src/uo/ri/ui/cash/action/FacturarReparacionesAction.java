package uo.ri.ui.cash.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import alb.util.console.Console;
import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.math.Round;
import alb.util.menu.Action;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.invoice.CreateInvoice;
import uo.ri.common.BusinessException;

public class FacturarReparacionesAction implements Action {

	@Override
	public void execute() throws BusinessException {
		LinkedList<BreakdownDto> breakdowns = new LinkedList<>();
		
		// pedir las averias a incluir en la factura
		do {
			BreakdownDto breakdown = new BreakdownDto();
			breakdown.id = Console.readLong("ID de averia");
			breakdowns.add(breakdown);
		} while ( masAverias() );

		CreateInvoice createInvoice = new CreateInvoice(breakdowns);
		InvoiceDto invoice = createInvoice.execute();

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
