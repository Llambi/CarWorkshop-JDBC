package uo.ri.ui.util;

import alb.util.console.Console;
import uo.ri.business.dto.*;
import uo.ri.persistence.impl.ContracStatus;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Printer {

    public static void printAddMechanic() {
        Console.println();
        Console.println("Nuevo mecánico añadido");
    }

    public static void printAddTypeContract() {
        Console.println();
        Console.println("Nuevo tipo de contrato añadido");
    }

    public static void printAddCategory() {
        Console.println();
        Console.println("Nueva categoria añadida");
    }

    public static void printDeleteMechanic() {
        Console.println();
        Console.println("Se ha eliminado el mecánico");
    }

    public static void printDeleteCategory() {
        Console.println();
        Console.println("Categoria eliminada");
    }

    public static void printDeleteTypeContract() {
        Console.println();
        Console.println("Tipo de contrato eliminado");
    }

    public static void printListMechanics(List<MechanicDto> list) {
        Console.println();
        Console.println("\nListado de mecánicos\n");
        for (MechanicDto dto : list) {
            Console.printf("\t%d %s %s\n", dto.id,
                    dto.name, dto.surname);
        }
    }

    public static void printUpdateMechanic() {
        Console.println();
        Console.println("Mecánico actualizado");
    }

    public static void printUpdateTypeContract() {
        Console.println();
        Console.println("Tipo de contrato actualizado");
    }

    public static void printUpdateCategory() {
        Console.println();
        Console.println("Categoria actualizada");
    }

    public static void printFacturarReparaciones(Map<String, Object> map) {
        Console.println();
        Console.printf("Factura nº: %d\n", map.get("numeroFactura"));
        Console.printf("\tFecha: %1$td/%1$tm/%1$tY\n", map.get("fechaFactura"));
        Console.printf("\tTotal: %.2f €\n", map.get("totalFactura"));
        Console.printf("\tIva: %.1f %% \n", map.get("iva"));
        Console.printf("\tTotal con IVA: %.2f €\n", map.get("totalConIva"));
    }

    public static void printBonosNumeroAverias() {
        Console.println();
        Console.println("Generados los bonos por 3 averias");
    }

    public static void printBonosCantidadFacturas() {
        Console.println();
        Console.println(
                "Generados los bonos para las facturas superiores a 500€");
    }

    public static void printAddClient() {
        Console.println();
        Console.println("Nuevo cliente añadido");
    }

    public static void printDeleteClient() {
        Console.println();
        Console.println("Se ha eliminado el cliente");
    }

    public static void printDetailClient(Map<String, Object> mapa) {
        Console.println();
        Console.println("Estos son los detalles del cliente");
        Console.println("Nombre: " + mapa.get("nombre"));
        Console.println("Apellidos: " + mapa.get("apellidos"));
        Console.println("DNI: " + mapa.get("dni"));
        Console.println("Codigo Postal: " + mapa.get("zipcode"));
        Console.println("Telefono: " + mapa.get("telefono"));
        Console.println("Correo electronico: " + mapa.get("email"));
    }

    public static void printListClients(List<Map<String, Object>> list) {
        Console.println();
        Console.println("\nListado de clientes\n");
        for (Map<String, Object> mapa : list) {
            Console.println("ID: " + mapa.get("id"));
            Console.println("Nombre: " + mapa.get("nombre"));
            Console.println("Apellidos: " + mapa.get("apellidos"));
            Console.println("DNI: " + mapa.get("dni"));
            Console.println("-----------------------");
        }
    }

    public static void printListClientRecomendations(Map<String, Object> mapa) {
        Console.println("Formato: Nombre - Apellidos - ID");
        Console.println("\nListado de clientes que ha recomendado\n");
        for (Map.Entry<String, Object> dato : mapa.entrySet()) {
            Console.println(dato.getValue());
        }
    }

    public static void printUpdateClient() {
        Console.println();
        Console.println("Cliente actualizado");
    }

    public static void printMostrarDetallesBonosCliente(
            List<Map<String, Object>> listaBonos) {
        Console.println();
        Console.println("\nListado de bonos\n");
        for (Map<String, Object> mapa : listaBonos) {
            if (mapa.get("tipo") != "InfoAgregada") {
                Console.printf("Codigo: %s\n", mapa.get("codigo"));
                Console.printf("Descripcion: %s\n", mapa.get("descripcion"));
                Console.printf("Importe disponible: %.2f€\n",
                        mapa.get("disponible"));
                Console.printf("Importe acumulado: %.2f€\n",
                        mapa.get("acumulado"));
                Console.println("-----------------------");
            } else {
                Console.println("\nInformacion Agregada\n");
                Console.printf("Numero de bonos: %d\n", mapa.get("numBonos"));
                Console.printf("Importe total: %.2f€\n", mapa.get("total"));
                Console.printf("Importe consumido: %.2f€\n",
                        mapa.get("consumido"));
                Console.printf("Importe restante: %.2f€\n",
                        mapa.get("restante"));
            }
        }

    }

    public static void printListarResumenBonos(
            List<Map<String, Object>> listaClientes) {
        Console.println();
        Console.println("\nListado de clientes y bonos\n");
        for (Map<String, Object> mapa : listaClientes) {
            Console.printf("DNI: %s\n", mapa.get("dni"));
            Console.printf("Nombre: %s\n", mapa.get("nombre"));
            Console.printf("Numero de bonos: %d\n", mapa.get("numBonos"));
            Console.printf("Importe total: %.2f€\n", mapa.get("total"));
            Console.printf("Importe consumido: %.2f€\n", mapa.get("consumido"));
            Console.printf("Importe restante: %.2f€\n", mapa.get("restante"));
            Console.println("-----------------------");
        }
    }

    public static void printLiquidarFactura() {
        Console.println();
        Console.println("La factura ha sido abonada correctamente");
    }

    public static void printMediosPago(List<Map<String, Object>> mediosPago) {
        Console.println("Medios de pago disponibles: ");
        for (int i = 0; i < mediosPago.size(); i++)
            Console.printf("%d) => %s\n", i + 1,
                    getInfoMedioPago(mediosPago.get(i)));
    }

    /**
     * Metodo auxiliar para imprimir los medios de pago
     *
     * @param medioPago Datos del medio de pago
     * @return datos del medio de pago formateados
     */
    private static String getInfoMedioPago(Map<String, Object> medioPago) {
        String tipo = (String) medioPago.get("dtype");
        if (tipo.equals("TBonos"))
            return String.format(
                    "Tipo: Bono --- Codigo: %s --- Disponible: %.2f",
                    medioPago.get("codigo"), medioPago.get("disponible"));
        else if (tipo.equals("TTarjetasCredito"))
            return String.format(
                    "Tipo: Tarjeta de Crédito --- Numero: %s --- Tipo: %s",
                    medioPago.get("numero"), medioPago.get("tipo"));
        else
            return "Tipo: Metálico";
    }

    /**
     * Metodo que devuelve los diferentes mensajes que se necesitan para
     * recaudar los distintos medios de pago de una factura.
     * <p>
     * Este metodo busca liberar a la interfaz de usuario de la mayor cantidad
     * posible de texto.
     *
     * @param codigo Codigo a mostrar por pantalla
     * @param extra
     */
    public static void printRecaudarMediosPago(String codigo, Double extra) {
        switch (codigo) {
            case "Total":
                Console.printf("Importe total: %.2f€\n", extra);
                break;
            case "Restante":
                Console.printf("Falta por pagar: %.2f€\n\n", extra);
                break;
            default:
                break;
        }
    }

    public static void printLiquidacion(Map<String, Object> liquidacion) {
        if (liquidacion != null)
            Console.printf("Se a liquidado un contrato del mecanico, con en siguiente resultado:\n" +
                            "\t· Salario bruto : %.2f€\n" +
                            "\t· Dias indemnizados : %d\n" +
                            "\t· Años de contrato : %d\n" +
                            "\t· Total : %.2f€\n\n", liquidacion.get("salarioBruto"), liquidacion.get("indemnizacion"),
                    liquidacion.get("añosContrato"), liquidacion.get("total"));
    }

    public static void printDeleteContract() {
        Console.println();
        Console.println("Se ha eliminado el contrato");
    }

    public static void printUpdateContract() {
        Console.println();
        Console.println("Se ha actualizado el contrato");
    }

    public static void printTerminateContract() {
        Console.println();
        Console.println("Se ha extinto el contrato");
    }

    public static void printListContractTypes(Map<ContractTypeDto, Map<MechanicDto, ContractDto>> contractTypeDtos) {
        Console.println();

        for (Map.Entry<ContractTypeDto, Map<MechanicDto, ContractDto>> entry : contractTypeDtos.entrySet()) {
            ContractTypeDto contractTypeDto = entry.getKey();
            double acumSalary = 0D;
            int mechanicsCounter = 0;
            Console.printf("Los siguientes mecanicos tienen el tipo de contrato %s:\n\n", contractTypeDto.name);

            for (Map.Entry<MechanicDto, ContractDto> meco : entry.getValue().entrySet()) {
                MechanicDto mechanicDto = meco.getKey();
                ContractDto contractDto = meco.getValue();
                Console.printf("\t%s %s\tDNI: %s\n", mechanicDto.name, mechanicDto.surname, mechanicDto.dni);
                acumSalary += contractDto.yearBaseSalary;
                mechanicsCounter++;
            }
            Console.println();
            Console.printf("\tEl numero total de mecanicos con este tipo de contrato son: %s\n", mechanicsCounter);
            Console.printf("\tEl salaruio acumulado de mecanicos con este tipo de contrato son: %s\n", acumSalary);
            Console.println();
        }
    }

    public static void printListCategories(Map<ContractCategoryDto, Map<MechanicDto, ContractDto>> contractTypeDtos) {
        Console.println();

        for (Map.Entry<ContractCategoryDto, Map<MechanicDto, ContractDto>> entry : contractTypeDtos.entrySet()) {
            ContractCategoryDto contractTypeDto = entry.getKey();
            double acumSalary = 0D;
            int mechanicsCounter = 0;
            Console.printf("Los siguientes mecanicos tienen la categoria %s:\n\n", contractTypeDto.name);

            for (Map.Entry<MechanicDto, ContractDto> meco : entry.getValue().entrySet()) {
                MechanicDto mechanicDto = meco.getKey();
                ContractDto contractDto = meco.getValue();
                Console.printf("\t%s %s\tDNI: %s\n", mechanicDto.name, mechanicDto.surname, mechanicDto.dni);
                acumSalary += contractDto.yearBaseSalary;
                mechanicsCounter++;
            }
            Console.println();
            Console.printf("\tEl numero total de mecanicos con esta categoria son: %s\n", mechanicsCounter);
            Console.printf("\tEl salaruio acumulado de mecanicos con esta categoria son: %s\n", acumSalary);
            Console.println();
        }
    }

    public static void printListContracts(Long id, Map<ContractDto, Map<String, Object>> contracts) {
        Console.println("El mecanico con ID " + id + " tiene los siguientes contratos:");
        for (Map.Entry<ContractDto, Map<String, Object>> entry : contracts.entrySet()) {
            ContractDto contractDto = entry.getKey();
            int payrolls = (int) entry.getValue().get("payrolls");
            double liquidacion = (double) entry.getValue().get("liquidacion");
            Console.println("\t·Contrato no." + contractDto.id + (contractDto.status.equalsIgnoreCase(ContracStatus.ACTIVE.toString()) ? " - ACTIVO" : ""));
            Console.println(payrolls > 0 ? "\t\t·Nominas expedidas " + payrolls + "€\n" : "\t\t·Sin nominas.");
            Console.println(liquidacion > 0.0 ? "\t\t·Liquidacion de " + liquidacion + "€\n" : "\t\t·Sin liquidacion.");
        }
    }

    public static void generetePayrolls() {
        Console.println();
        Console.println("Nominas generadas");
    }

    public static void printListPayrolls(Long id, List<PayrollDto> payrollDtos) {
        Console.println();
        Console.println("El mecanico con ID " + id + " tiene las siguientes nominas:\n");
        for (PayrollDto p : payrollDtos) {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
            cal.setTime(p.date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            Console.println("\t·ID: " + p.id + "\tAño " + year + "\tMes: " + month + " \tCantidad neta percibida: " + p.netTotal);
        }
    }

    public static void printPayroll(PayrollDto p) {
        Console.println();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        cal.setTime(p.date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        Console.println("Nomina con ID: " + p.id + "\tAño " + year + "\tMes: " + month + "\n");
        Console.println("\t·TOTAL BRUTO: " + p.grossTotal);
        Console.println("\t\t·Salario base: " + p.baseSalary);
        Console.println("\t\t·Paga Extra: " + p.extraSalary);
        Console.println("\t\t·Plus productividad: " + p.productivity);
        Console.println("\t\t·Trienios: " + p.triennium);
        Console.println();
        Console.println("\t·DESCUENTOS: " + p.discountTotal);
        Console.println("\t\t·IRPF: " + p.irpf);
        Console.println("\t\t·Seguridad Social: " + p.socialSecurity);
        Console.println();
        Console.println("\t·TOTAL NETO: " + p.netTotal);

    }

    public static void printDeleteMechanicPayroll() {
        Console.println();
        Console.println("Nomina eliminada");
    }

    public static void printDeletePayrolls() {
        Console.println();
        Console.println("Nominas eliminadas");
    }
}
