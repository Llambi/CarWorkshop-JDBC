package uo.ri.ui.admin.action.payroll;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.PayrollDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class FindPayrollAction implements Action {
    @Override
    public void execute() throws Exception {
        long id = Console.readLong("ID de la nomina");
        PayrollDto payrollDto =
                new ServiceFactory().forPayroll().findPayrollById(id);
        if (payrollDto != null)
            Printer.printPayroll(payrollDto);
    }
}
