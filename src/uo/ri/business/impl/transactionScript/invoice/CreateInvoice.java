package uo.ri.business.impl.transactionScript.invoice;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.math.Round;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.lang.reflect.GenericArrayType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CreateInvoice {

    private List<Long> ids;
    private Connection connection;

    public CreateInvoice(List<Long> ids) {
        this.ids = ids;
    }

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

    private void verificarAveriasTerminadas(List<Long> ids) throws BusinessException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        BreakdownGateway breakdownGateway = GatewayFactory.getBreakdownGateway();
        for (Long id : ids) {
            BreakdownDto breakdown = null;

            try {
                breakdown = breakdownGateway.findBreakdown(id);
            } catch (PersistanceException e) {
                throw new BusinessException("No existe la averia:\n\t" + e.getStackTrace());
            }

            if (!"TERMINADA".equalsIgnoreCase(breakdown.status)) {
                throw new BusinessException("No está terminada la avería " + id);
            }
        }


    }

    private void cambiarEstadoAverias(List<Long> ids) throws BusinessException {

        for (Long id : ids) {
            try {
                GatewayFactory.getBreakdownGateway().updateBreakdown(id, "status", "FACTURADA");
            } catch (PersistanceException e) {
                throw new BusinessException("Averia no actualizada:\n\t" + e.getStackTrace());
            }
        }

    }

    private void vincularAveriasConFactura(long idFactura, List<Long> ids) throws BusinessException {

        for (Long id : ids) {
            try {
                GatewayFactory.getBreakdownGateway().updateBreakdown(id, "factura_id", idFactura);
            } catch (PersistanceException e) {
                throw new BusinessException("Averia no vinculada:\n\t" + e.getStackTrace());
            }
        }
    }

    private long crearFactura(InvoiceDto invoice) throws BusinessException {
        Long numero = null;
        try {
            numero = getGeneratedKey(GatewayFactory.getInvoiceGateway().createInvoice(invoice).number);
        } catch (PersistanceException e) {
            throw new BusinessException("Factura no creada:\n\t" + e.getStackTrace());
        }
        return numero;
    }

    private long getGeneratedKey(long numeroFactura) throws BusinessException {

        try {
            return GatewayFactory.getInvoiceGateway().ListInvoice(numeroFactura).id;
        } catch (PersistanceException e) {
            throw new BusinessException("Clave no generada:\n\t" + e.getStackTrace());
        }

    }

    private Long generarNuevoNumeroFactura() throws BusinessException {
        try {
            return GatewayFactory.getInvoiceGateway().ListLastInvoice();
        } catch (PersistanceException e) {
            throw new BusinessException("Error al generar un nuevo numero de factura:\n\t" + e.getStackTrace());
        }

    }

    private double porcentajeIva(Date fechaFactura) {
        return Dates.fromString("1/7/2012").before(fechaFactura) ? 21.0 : 18.0;
    }

    private double calcularImportesAverias(List<Long> ids)
            throws BusinessException, SQLException {

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

    private void actualizarImporteAveria(Long idAveria, double totalAveria) throws BusinessException {

        try {
            GatewayFactory.getBreakdownGateway().updateBreakdown(idAveria, "importe", totalAveria);
        } catch (PersistanceException e) {
            throw new BusinessException("No se ha actualizado el importe de la averia:\n\t"+ e.getStackTrace());
        }

    }

    private double consultaImporteRepuestos(Long idAveria) throws BusinessException {

        try {
            return GatewayFactory.getSpareGateway().getSpareTotalImport(idAveria);
        } catch (PersistanceException e) {
            throw new BusinessException("Error en el calculo del importe de los repuestos:\n\t"+e.getStackTrace());
        }

    }

    private double consultaImporteManoObra(Long idAveria) throws BusinessException {

        try {
            return GatewayFactory.getInterventionGateway().getManPowerTotalImport(idAveria);
        } catch (PersistanceException e) {
            throw new BusinessException("Error en el calculo del importe de la mano de obra:\n\t"+e.getStackTrace());
        }

    }
}
