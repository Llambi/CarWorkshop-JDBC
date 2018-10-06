package uo.ri.ui.cash.action;

import alb.util.console.Console;
import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReparacionesNoFacturadasUnClienteAction implements Action {

    // Para pruebas dni: 599414936
    private final static String SQL_REPARACIONES_NO_FACTURADAS_CLIENTE = "SELECT a.ID, a.FECHA, a.STATUS, a.IMPORTE, a.DESCRIPCION\n" +
            "FROM TCLIENTES c\n" +
            "       INNER JOIN TVEHICULOS v ON c.ID = v.CLIENTE_ID\n" +
            "       INNER JOIN TAVERIAS a on v.ID = a.VEHICULO_ID\n" +
            "       INNER JOIN TFACTURAS f on a.FACTURA_ID = f.ID AND f.STATUS <> 'ABONADA'\n" +
            "WHERE c.DNI = ?;";
    private Connection connection;

    /**
     * Proceso:
     * <p>
     * - Pide el DNI del cliente
     * <p>
     * - Muestra en pantalla todas sus averias no facturadas
     * (status <> 'FACTURADA'). De cada avería muestra su
     * id, fecha, status, importe y descripción
     */
    @Override
    public void execute() {


        String dni = Console.readString("DNI del cliente");

        try {
            connection = Jdbc.getConnection();
            connection.setAutoCommit(false);

            obtenerAveriasNoFacturadas(dni);

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
    }

    private void obtenerAveriasNoFacturadas(String dni) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement(SQL_REPARACIONES_NO_FACTURADAS_CLIENTE);

            pst.setString(1, dni);

            rs = pst.executeQuery();
            Console.printf("El cliente con DNI: %s tiene las siguientes facturas sin pagar:\n\n", dni);
            Console.println("ID - FECHA - STATUS - IMPORTE - DESCRIPCION\n");
            while (rs.next()) {
                Console.printf("%s - %s - %s - %s - %s"
                        , rs.getLong(1)
                        , rs.getString(2)
                        , rs.getString(3)
                        , rs.getString(4)
                        , rs.getString(5));
            }
            Console.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

}
