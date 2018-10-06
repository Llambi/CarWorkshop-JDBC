package uo.ri.business.mechanic;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateMechanic {

    private static String SQL =
            "update TMecanicos " +
                    "set nombre = ?, apellidos = ? " +
                    "where id = ?";
    private MechanicDto mechanic;

    public UpdateMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute(){
        // Procesar
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(SQL);
            pst.setString(1, mechanic.name);
            pst.setString(2, mechanic.surname);
            pst.setLong(3, mechanic.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            Jdbc.close(rs, pst, c);
        }
    }
}
