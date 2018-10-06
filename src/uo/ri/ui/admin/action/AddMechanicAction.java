package uo.ri.ui.admin.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import alb.util.console.Console;
import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.mechanic.AddMechanic;
import uo.ri.common.BusinessException;

public class AddMechanicAction implements Action {


    @Override
    public void execute() throws BusinessException {

        MechanicDto mechanicDto = new MechanicDto();

        // Pedir datos
        mechanicDto.dni = Console.readString("Dni");
        mechanicDto.name = Console.readString("Nombre");
        mechanicDto.surname = Console.readString("Apellidos");

        AddMechanic addMechanic = new AddMechanic(mechanicDto);
        addMechanic.execute();

        // Mostrar resultado
        Console.println("Nuevo mecánico añadido");
    }

}
