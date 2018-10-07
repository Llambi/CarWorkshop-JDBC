package uo.ri.ui.admin.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.MechanicCRUDService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.impl.MechanicCRUDImpl;
import uo.ri.business.impl.mechanic.UpdateMechanic;
import uo.ri.common.BusinessException;
import uo.ri.conf.ServiceFactory;

public class UpdateMechanicAction implements Action {



	@Override
	public void execute() throws BusinessException {

		MechanicDto mechanic = new MechanicDto();

		// Pedir datos
		mechanic.id = Console.readLong("Id del mecánico");
		mechanic.name = Console.readString("Nombre");
		mechanic.surname = Console.readString("Apellidos");

		new ServiceFactory().getMechanicCRUDService().updateMechanic(mechanic);
		
		// Mostrar resultado
		Console.println("Mecánico actualizado");
	}

}
