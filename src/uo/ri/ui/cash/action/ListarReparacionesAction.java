package uo.ri.ui.cash.action;

import alb.util.console.Console;
import alb.util.date.Dates;
import alb.util.menu.Action;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.ServiceFactory;

import java.util.List;

public class ListarReparacionesAction implements Action {

    // Para pruebas dni: 222620568

    /**
     * Proceso:
     * <p>
     * - Pide el DNI del cliente
     * <p>
     * - Muestra en pantalla todas sus averias no facturadas
     * (status <> 'FACTURADA'). De cada avería muestra su
     * id, fecha, status, importe y descripción
     */
    @Override
    public void execute() throws BusinessException {

       String dni = Console.readString("DNI del cliente");

        List<BreakdownDto> breakdowns = new ServiceFactory().forInvoice().findRepairsByClient(dni);

        Console.printf("El cliente con DNI: %s tiene las siguientes facturas sin pagar:\n\n", dni);
        Console.println("ID - FECHA - STATUS - IMPORTE - DESCRIPCION\n");

        for (BreakdownDto breakdown : breakdowns) {
            Console.printf("%s - %s - %s - %s - %s"
                    , breakdown.id
                    , Dates.toString(breakdown.date)
                    , breakdown.status
                    , breakdown.total
                    , breakdown.description);
        }

        Console.println();

    }

}
