package uo.ri.ui.admin.action.mechanic;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.List;

public class ListActiveMechanicsAction implements Action {
    @Override
    public void execute() throws Exception {
        Console.println("\nListado de mecánicos\n");

        List<MechanicDto> listMechanics = new ServiceFactory().forMechanicCrudService().findActiveMechanics();

        Printer.printListMechanics(listMechanics);
    }
}
