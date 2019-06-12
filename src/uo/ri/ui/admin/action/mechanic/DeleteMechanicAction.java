package uo.ri.ui.admin.action.mechanic;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para eliminar un mecanico.
 */
public class DeleteMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {

        Long id;
        id = Console.readLong("Id de mecánico");

        new ServiceFactory().forMechanicCrudService().deleteMechanic(id);

        Printer.printDeleteMechanic();
    }

}
