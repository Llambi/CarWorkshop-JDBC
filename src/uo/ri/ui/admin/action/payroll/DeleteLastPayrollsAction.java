package uo.ri.ui.admin.action.payroll;

import alb.util.menu.Action;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class DeleteLastPayrollsAction implements Action {
    @Override
    public void execute() throws Exception {
        new ServiceFactory().forPayroll().deleteLastGenetaredPayrolls();
        Printer.printDeletePayrolls();
    }
}
