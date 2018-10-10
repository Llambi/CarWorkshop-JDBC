package uo.ri.ui.admin.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.ServiceFactory;

public class DeleteMechanicAction implements Action {


    @Override
    public void execute() throws BusinessException {

        MechanicDto mechanic = new MechanicDto();
        mechanic.id = Console.readLong("Id de mecánico");

        new ServiceFactory().getMechanicCRUDService().deleteMechanic(mechanic);

        Console.println("Se ha eliminado el mecánico");
    }

}
