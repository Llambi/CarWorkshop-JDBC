package uo.ri.persistence.impl;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.InvoiceGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que contiene la persistencia de las facturas.
 */
public class InvoiceGatewayImpl implements InvoiceGateway {
    /**
     * Metodo que inserta una factura.
     *
     * @param invoice informacion de la factura a insertar.
     * @return Factura que se ha insertado.
     * @throws PersistanceException
     */
    @Override
    public InvoiceDto createInvoice(InvoiceDto invoice)
            throws PersistanceException {
        PreparedStatement pst = null;

        try {
            pst = Jdbc.getCurrentConnection()
                    .prepareStatement(Conf.getInstance()
                            .getProperty("SQL_INSERTAR_FACTURA"));
            pst.setLong(1, invoice.number);
            pst.setDate(2, new java.sql.Date
                    (invoice.date.getTime()));
            pst.setDouble(3, invoice.vat);
            pst.setDouble(4, invoice.total);
            pst.setString(5, "SIN_ABONAR");
            invoice.status = "SIN_ABONAR";

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Factura no insertada:\n\t" + e);
        } finally {
            Jdbc.close(pst);
        }
        return invoice;
    }

    /**
     * Metodo que recupera una factura por su numero.
     *
     * @param number Numero de la factura.
     * @return Factura que se ha encontrado.
     * @throws PersistanceException
     */
    @Override
    public InvoiceDto listInvoice(Long number)
            throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        InvoiceDto resultInvoice = new InvoiceDto();
        try {

            pst = Jdbc.getCurrentConnection()
                    .prepareStatement(Conf.getInstance()
                            .getProperty("SQL_INVOICE"));
            pst.setLong(1, number);

            rs = pst.executeQuery();

            if (!rs.next()) {
                throw new PersistanceException
                        ("No existe la factura con numero: " + number);
            }

            resultInvoice.id = rs.getLong(1);
            resultInvoice.date = Dates.fromString(rs.getString(2));
            resultInvoice.amount = rs.getLong(3);
            resultInvoice.vat = rs.getLong(4);
            resultInvoice.number = rs.getLong(5);
            resultInvoice.status = rs.getString(6);
            resultInvoice.total = resultInvoice.amount * (1 +
                    (resultInvoice.vat / 100));

        } catch (SQLException e) {
            throw new PersistanceException
                    ("Error al recuperar la factura:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return resultInvoice;
    }

    /**
     * Metodo que recupera la ultima factura creada.
     *
     * @return Factura que se ha encontrado.
     * @throws PersistanceException
     */
    @Override
    public Long listLastInvoice() throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = Jdbc.getCurrentConnection()
                    .prepareStatement(Conf.getInstance()
                    .getProperty("SQL_ULTIMO_NUMERO_FACTURA"));
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getLong(1) + 1; // +1, el siguiente
            } else {  // todavía no hay ninguna
                return 1L;
            }
        } catch (SQLException e) {
            throw new PersistanceException
                    ("Problemas al recuperar la ultima factura:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    /**
     * Metodo que actualiza una factura dado su identificador.
     *
     * @param campo  Campo que se quiere actualizar.
     * @param estado Valor al que se quiere actualizar el campo.
     * @param id     Identificador de la factura a actualizar.
     * @throws PersistanceException
     */
    @Override
    public void updateInvoice(String campo, String estado, Long id)
            throws PersistanceException {
        PreparedStatement pst = null;

        try {
            pst = Jdbc.getCurrentConnection()
                    .prepareStatement(Conf.getInstance()
                    .getProperty("SQL_UPDATE_INVOICE_ABONADA"));
//            pst.setString(1, campo);
            pst.setString(1, estado);
            pst.setLong(2, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException
                    ("Factura no actualizada:\n\t" + e);
        } finally {
            Jdbc.close(pst);
        }
    }
}
