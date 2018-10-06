package uo.ri.ui.admin.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import alb.util.console.Console;
import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.mechanic.ListMechanics;
import uo.ri.common.BusinessException;

public class ListMechanicsAction implements Action {


    @Override
    public void execute() {

        Console.println("\nListado de mecánicos\n");

        LinkedList<MechanicDto> listMechanics = new ListMechanics().execute();

        for (MechanicDto mechanic : listMechanics) {
            Console.printf("%d - %s - %s - %s\n",
                    mechanic.id,
                    mechanic.dni,
                    mechanic.name,
                    mechanic.surname);
        }
    }
}
