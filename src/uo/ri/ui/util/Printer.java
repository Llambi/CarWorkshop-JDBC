package uo.ri.ui.util;

import java.util.List;
import java.util.Map;

import alb.util.console.Console;

public class Printer {

	public static void printAddMechanic() {
		Console.println();
		Console.println("Nuevo mecánico añadido");
	}

	public static void printDeleteMechanic() {
		Console.println();
		Console.println("Se ha eliminado el mecánico");
	}

	public static void printListMechanics(List<Map<String, Object>> list) {
		Console.println();
		Console.println("\nListado de mecánicos\n");
		for (Map<String, Object> map : list) {
			Console.printf("\t%d %s %s\n", map.get("idMecanico"),
					map.get("nombre"), map.get("apellidos"));
		}
	}

	public static void printUpdateMechanic() {
		Console.println();
		Console.println("Mecánico actualizado");
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
	 * @param mapa Datos del medio de pago
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
	 * 
	 * Este metodo busca liberar a la interfaz de usuario de la mayor cantidad 
	 * posible de texto.
	 * 
	 * @param codigo Codigo a mostrar por pantalla
	 * @param object 
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
}
