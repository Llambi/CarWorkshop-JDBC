package uo.ri.ui.admin.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.ServiceFactory;

public class AddMechanicAction implements Action {


    @Override
    public void execute() throws BusinessException {

        MechanicDto mechanicDto = new MechanicDto();

        // Pedir datos
        mechanicDto.dni = Console.readString("Dni");
        mechanicDto.name = Console.readString("Nombre");
        mechanicDto.surname = Console.readString("Apellidos");

        new ServiceFactory().getMechanicCRUDService().addMechanic(mechanicDto);

        // Mostrar resultado
        Console.println("Nuevo mecánico añadido");
    }

}
