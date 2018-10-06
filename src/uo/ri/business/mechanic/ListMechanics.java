package uo.ri.business.mechanic;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ListMechanics {

    private static String SQL = "select id, dni, nombre, apellidos from TMecanicos";

    public LinkedList<MechanicDto> execute() {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        LinkedList<MechanicDto> mechanics = new LinkedList<MechanicDto>();

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(SQL);

            rs = pst.executeQuery();
            while (rs.next()) {
                MechanicDto mechanic = new MechanicDto();
                mechanic.id = rs.getLong(1);
                mechanic.dni = rs.getString(2);
                mechanic.name = rs.getString(3);
                mechanic.surname = rs.getString(4);
                mechanics.add(mechanic);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
        return mechanics;
    }
}
