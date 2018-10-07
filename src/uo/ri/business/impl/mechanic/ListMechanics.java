package uo.ri.business.impl.mechanic;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class ListMechanics {

    public LinkedList<MechanicDto> execute() {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        LinkedList<MechanicDto> mechanics = new LinkedList<MechanicDto>();

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_ALL_MECHANICS"));

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
