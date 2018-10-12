package uo.ri.business.impl.transactionScript.invoice;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

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

    public List<BreakdownDto> execute() throws BusinessException {

        List<BreakdownDto> breakdowns = null;

        try {
            connection = Jdbc.createThreadConnection();
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

    private List<BreakdownDto> obtenerAveriasNoFacturadas(Long id) throws BusinessException {

        try {
            return GatewayFactory.getBreakdownGateway().findUninvoicedBreakdown(id);
        } catch (PersistanceException e) {
            throw new BusinessException("Fallo al recuperar las averias no facturadas:\n\t"+e.getStackTrace());
        }

    }
}

