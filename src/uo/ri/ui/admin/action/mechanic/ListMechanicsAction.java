package uo.ri.ui.admin.action.mechanic;

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para listar los mecanicos.
 */
public class ListMechanicsAction implements Action {


    @Override
    public void execute() throws BusinessException {

        Console.println("\nListado de mecánicos\n");

        List<MechanicDto> listMechanics = new ServiceFactory()
                .forMechanicCrudService().findAllMechanics();

        Printer.printListMechanics(listMechanics);

    }
}
