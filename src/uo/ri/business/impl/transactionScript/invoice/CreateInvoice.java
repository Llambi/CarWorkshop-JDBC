package uo.ri.business.impl.transactionScript.invoice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.math.Round;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.exception.PersistanceException;

/**
 * Clase que contiene la logica para la creacion de una factura
 */
public class CreateInvoice {

    private List<Long> ids;
    private Connection connection;

    public CreateInvoice(List<Long> ids) {
        this.ids = ids;
    }

    /**
     * Metodo que realiza las operaciones necesarias para crear una
     * factura.
     *
     * @return Una InvoiceDto con la informacion de la nueva factura.
     * @throws BusinessException
     */
    public InvoiceDto execute() throws BusinessException {
        InvoiceDto invoice = new InvoiceDto();

        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            verificarAveriasTerminadas(ids);


            invoice.number = generarNuevoNumeroFactura();
            invoice.date = Dates.today();
            invoice.amount = calcularImportesAverias(ids);
            invoice.vat = porcentajeIva(invoice.date);
            double importe = invoice.amount * (1 + invoice.vat / 100);
            invoice.total = Round.twoCents(importe);

            invoice.id = crearFactura(invoice);
            vincularAveriasConFactura(invoice.id, ids);
            cambiarEstadoAverias(ids);

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException(e);
        } catch (BusinessException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw e;
        } finally {
            Jdbc.close(connection);
        }
        return invoice;
    }

    /**
     * Metodo que comprueba que las averias estan terminadas.
     *
     * @param ids De las averias a compreobar.
     * @throws BusinessException
     */
    private void verificarAveriasTerminadas(List<Long> ids)
            throws BusinessException {
        BreakdownGateway breakdownGateway = GatewayFactory
                .getBreakdownGateway();
        for (Long id : ids) {
            BreakdownDto breakdown = null;

            try {
                breakdown = breakdownGateway.findBreakdown(id);
            } catch (PersistanceException e) {
                throw new BusinessException
                        ("No existe la averia:\n\t" + e);
            }

            if (!"TERMINADA".equalsIgnoreCase(breakdown.status)) {
                throw new BusinessException
                        ("No está terminada la avería " + id);
            }
        }


    }

    /**
     * Metodo que modifica el estado de las averias a FACTURADA.
     *
     * @param ids De las averias a modificar.
     * @throws BusinessException
     */
    private void cambiarEstadoAverias(List<Long> ids)
            throws BusinessException {

        for (Long id : ids) {
            try {
                GatewayFactory.getBreakdownGateway()
                        .updateBreakdown(id, "status",
                                "FACTURADA");
            } catch (PersistanceException e) {
                throw new BusinessException
                        ("Averia no actualizada:\n\t" + e);
            }
        }

    }

    /**
     * Metodo que añade la factura a las averias.
     *
     * @param idFactura De la factura que se quiere añadir a las averias
     * @param ids       De las averias a las que se le añadiran la factura
     * @throws BusinessException
     */
    private void vincularAveriasConFactura(long idFactura, List<Long> ids)
            throws BusinessException {

        for (Long id : ids) {
            try {
                GatewayFactory.getBreakdownGateway()
                        .updateBreakdown(id, "factura_id",
                                idFactura);
            } catch (PersistanceException e) {
                throw new BusinessException("Averia no vinculada:\n\t" + e);
            }
        }
    }

    /**
     * Metodo que genera la factura
     *
     * @param invoice InvoiceDto que contiene la informacion de la
     *                nueva factura
     * @return numero identificativo de la factura
     * @throws BusinessException
     */
    private long crearFactura(InvoiceDto invoice)
            throws BusinessException {
        Long numero = null;
        try {
            numero = getGeneratedKey(GatewayFactory.getInvoiceGateway()
                    .createInvoice(invoice).number);
        } catch (PersistanceException e) {
            throw new BusinessException("Factura no creada:\n\t" + e);
        }
        return numero;
    }

    /**
     * Metodo que genera el id de la factura.
     *
     * @param numeroFactura numero de la factura de la que se quiere
     *                      facturar el id.
     * @return Identificador de la factura.
     * @throws BusinessException
     */
    private long getGeneratedKey(long numeroFactura)
            throws BusinessException {

        try {
            return GatewayFactory.getInvoiceGateway()
                    .listInvoice(numeroFactura).id;
        } catch (PersistanceException e) {
            throw new BusinessException("Clave no generada:\n\t" + e);
        }

    }

    /**
     * Metodo que genera un nuevo numero para la factura.
     *
     * @return Numero de la factura.
     * @throws BusinessException
     */
    private Long generarNuevoNumeroFactura() throws BusinessException {
        try {
            return GatewayFactory.getInvoiceGateway().listLastInvoice();
        } catch (PersistanceException e) {
            throw new BusinessException
                    ("Error al generar un nuevo numero de" +
                            " factura:\n\t" + e);
        }

    }

    /**
     * Metodo que devuelve el porcentaje de IVA correspondiente
     * a la fecha de la factura.
     *
     * @param fechaFactura Fecha de la creacion de la factura.
     * @return POrcentage de IVA.
     */
    private double porcentajeIva(Date fechaFactura) {
        return Dates.fromString("1/7/2012")
                .before(fechaFactura) ? 21.0 : 18.0;
    }

    /**
     * Metodo que calcula el importe de la factura.
     *
     * @param ids Identificadores de las averias de la factura.
     * @return Double con el importe total de la factura.
     * @throws BusinessException
     */
    private double calcularImportesAverias(List<Long> ids)
            throws BusinessException {

        double totalFactura = 0.0;
        for (Long id : ids) {
            double importeManoObra = consultaImporteManoObra(id);
            double importeRepuestos = consultaImporteRepuestos(id);
            double totalAveria = importeManoObra + importeRepuestos;

            actualizarImporteAveria(id, totalAveria);

            totalFactura += totalAveria;
        }
        return totalFactura;
    }

    /**
     * Metodo que actualiza el importe de las averias.
     *
     * @param idAveria    Identificadores de la averias a modificar.
     * @param totalAveria Importe total de las averias.
     * @throws BusinessException
     */
    private void actualizarImporteAveria(Long idAveria,
                                         double totalAveria)
            throws BusinessException {

        try {
            GatewayFactory.getBreakdownGateway()
                    .updateBreakdown(idAveria, "importe",
                            totalAveria);
        } catch (PersistanceException e) {
            throw new BusinessException
                    ("No se ha actualizado el importe de la" +
                            " averia:\n\t" + e);
        }

    }

    /**
     * Metodo que comprueba el importe de los repuestos.
     *
     * @param idAveria Identificadores de las averias.
     * @return Double el con importe de los repuestos de una averia.
     * @throws BusinessException
     */
    private double consultaImporteRepuestos(Long idAveria)
            throws BusinessException {

        try {
            return GatewayFactory.getSpareGateway()
                    .getSpareTotalImport(idAveria);
        } catch (PersistanceException e) {
            throw new BusinessException
                    ("Error en el calculo del importe de " +
                            "los repuestos:\n\t" + e);
        }

    }

    /**
     * Metodo que comprueba el importe de la mano de obra
     *
     * @param idAveria Identificadores de las averias para
     *                 consultar su importe de mano de obra
     * @return Double con el importe total de la mano de
     * obra de las averias dadas.
     * @throws BusinessException
     */
    private double consultaImporteManoObra(Long idAveria)
            throws BusinessException {

        try {
            return GatewayFactory.getInterventionGateway()
                    .getManPowerTotalImport(idAveria);
        } catch (PersistanceException e) {
            throw new BusinessException
                    ("Error en el calculo del importe de " +
                            "la mano de obra:\n\t" + e);
        }

    }
}
