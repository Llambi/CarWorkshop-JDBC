package uo.ri.ui.admin.action.mechanic;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para actualizar un mecanico.
 */
public class UpdateMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {

        MechanicDto mechanic = new MechanicDto();

        // Pedir datos
        mechanic.id = Console.readLong("Id del mecánico");
        mechanic.name = Console.readString("Nombre");
        mechanic.surname = Console.readString("Apellidos");

        new ServiceFactory().forMechanicCrudService().updateMechanic(mechanic);

        // Mostrar resultado
        Printer.printUpdateMechanic();
    }

}
