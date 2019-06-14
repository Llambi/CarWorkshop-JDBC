package uo.ri.ui.admin.action.payroll;

import alb.util.menu.Action;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class GeneratePayrollsAction implements Action {
    @Override
    public void execute() throws Exception {
        int count = new ServiceFactory().forPayroll().generatePayrolls();
        Printer.generetePayrolls(count);
    }
}
