package uo.ri.ui.admin.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import alb.util.console.Console;
import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.mechanic.DeleteMechanic;
import uo.ri.common.BusinessException;

public class DeleteMechanicAction implements Action {


    @Override
    public void execute() throws BusinessException {

        MechanicDto mechanic = new MechanicDto();
        mechanic.id = Console.readLong("Id de mecánico");

        DeleteMechanic deleteMechanic = new DeleteMechanic(mechanic);
        deleteMechanic.execute();

        Console.println("Se ha eliminado el mecánico");
    }

}
