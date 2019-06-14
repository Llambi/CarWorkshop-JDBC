package uo.ri.ui.admin.action.payroll;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.PayrollDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.List;

public class ListPayrollsAction implements Action {
    @Override
    public void execute() throws Exception {
        long id = Console.readLong("ID del mecanico");
        List<PayrollDto> payrollDtos =
                new ServiceFactory().forPayroll()
                        .findPayrollsByMechanicId(id);

        Printer.printListPayrolls(id, payrollDtos);
    }
}
