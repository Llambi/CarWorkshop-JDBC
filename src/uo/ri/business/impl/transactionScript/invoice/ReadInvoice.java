package uo.ri.business.impl.transactionScript.invoice;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase que contiene la logica para leer una factura.
 */
public class ReadInvoice {

    private Connection connection;
    private Long id;

    public ReadInvoice(Long id) {
        this.id = id;
    }

    /**
     * Metodo que obtiene las averias no facturadas de un cliente.
     *
     * @return Averias no facturadas del cliente dado
     * @throws BusinessException
     */
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

    /**
     * Metodo que obtiene las averias no facturadasde un cliente.
     *
     * @param id Identificador del cliente.
     * @return Lista de averias sin facturar del cliente.
     * @throws BusinessException
     */
    private List<BreakdownDto> obtenerAveriasNoFacturadas(Long id) throws BusinessException {

        try {
            return GatewayFactory.getBreakdownGateway().findUninvoicedBreakdown(id);
        } catch (PersistanceException e) {
            throw new BusinessException("Fallo al recuperar las averias no facturadas:\n\t" + e);
        }

    }
}

