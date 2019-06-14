package uo.ri.ui.admin.action.payroll;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class DeleteMechanicPayrollAction implements Action {
    @Override
    public void execute() throws Exception {
        Long id = Console
                .readLong("Id del Mecanico del que " +
                        "se quiere eliminar la ultima nomina");

        new ServiceFactory().forPayroll().deleteLastPayrollForMechanicId(id);

        Printer.printDeleteMechanicPayroll();
    }
}
