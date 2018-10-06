package uo.ri.business.mechanic;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteMechanic {
    private static String SQL = "delete from TMecanicos where id = ?";
    private MechanicDto mechanic;

    public DeleteMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute() {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(SQL);
            pst.setLong(1, mechanic.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }
}
