package uo.ri.business.impl.invoice;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.conf.Conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ReadInvoice {

    private Connection connection;
    private Long id;

    public ReadInvoice(Long id) {
        this.id = id;
    }

    public List<BreakdownDto> execute() {

        List<BreakdownDto> breakdowns = null;

        try {
            connection = Jdbc.getConnection();
            connection.setAutoCommit(false);

            breakdowns = obtenerAveriasNoFacturadas(id);

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(e);
            }
        } finally {
            Jdbc.close(connection);
        }

        return breakdowns;
    }

    private List<BreakdownDto> obtenerAveriasNoFacturadas(Long id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_AVERIAS_NO_FACTURADAS_CLIENTE"));

            pst.setLong(1, id);

            rs = pst.executeQuery();

            List<BreakdownDto> breakdowns = new LinkedList<>();
            while (rs.next()) {
                BreakdownDto breakdown = new BreakdownDto();
                breakdown.id = rs.getLong(1);
                breakdown.date = Dates.fromString(rs.getString(2));
                breakdown.status = rs.getString(3);
                breakdown.total = Double.parseDouble(rs.getString(4));
                breakdown.description = rs.getString(5);

                breakdowns.add(breakdown);
            }

            return breakdowns;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }
}

