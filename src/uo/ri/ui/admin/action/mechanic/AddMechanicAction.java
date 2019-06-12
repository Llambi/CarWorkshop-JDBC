package uo.ri.ui.admin.action.mechanic;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para añadir un mecanico.
 */
public class AddMechanicAction implements Action {


    @Override
    public void execute() throws BusinessException {

        MechanicDto mechanicDto = new MechanicDto();

        // Pedir datos
        mechanicDto.dni = Console.readString("Dni");
        mechanicDto.name = Console.readString("Nombre");
        mechanicDto.surname = Console.readString("Apellidos");

        new ServiceFactory().forMechanicCrudService().addMechanic(mechanicDto);

        // Mostrar resultado
        Printer.printAddMechanic();
    }

}
