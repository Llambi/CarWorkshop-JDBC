package uo.ri.ui.admin.action;

import java.util.LinkedList;
import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.MechanicCRUDService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.impl.MechanicCRUDImpl;
import uo.ri.business.impl.mechanic.ListMechanics;
import uo.ri.conf.ServiceFactory;

public class ListMechanicsAction implements Action {


    @Override
    public void execute() {

        Console.println("\nListado de mecánicos\n");

        List<MechanicDto> listMechanics = new ServiceFactory().getMechanicCRUDService().findAllMechanics();

        for (MechanicDto mechanic : listMechanics) {
            Console.printf("%d - %s - %s - %s\n",
                    mechanic.id,
                    mechanic.dni,
                    mechanic.name,
                    mechanic.surname);
        }
    }
}
