package uo.ri.ui.admin.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import alb.util.console.Console;
import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.mechanic.UpdateMechanic;
import uo.ri.common.BusinessException;

public class UpdateMechanicAction implements Action {



	@Override
	public void execute() throws BusinessException {

		MechanicDto mechanic = new MechanicDto();

		// Pedir datos
		mechanic.id = Console.readLong("Id del mecánico");
		mechanic.name = Console.readString("Nombre");
		mechanic.surname = Console.readString("Apellidos");

		UpdateMechanic updateMechanic = new UpdateMechanic(mechanic);
		updateMechanic.execute();
		
		// Mostrar resultado
		Console.println("Mecánico actualizado");
	}

}
