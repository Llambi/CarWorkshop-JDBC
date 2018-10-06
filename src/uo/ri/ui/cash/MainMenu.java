package uo.ri.ui.cash;

import alb.util.menu.BaseMenu;
import alb.util.menu.NotYetImplementedAction;
import uo.ri.ui.cash.action.FacturarReparacionesAction;
import uo.ri.ui.cash.action.LiquidarFacturaAction;
import uo.ri.ui.cash.action.ReparacionesNoFacturadasUnClienteAction;

public class MainMenu extends BaseMenu {

	public MainMenu() {
		menuOptions = new Object[][] { 
			{ "Caja de Taller", null },
			{ "Buscar reparaciones no facturadas de un cliente", ReparacionesNoFacturadasUnClienteAction.class }, 
			{ "Buscar reparación por matrícula", 	NotYetImplementedAction.class }, 
			{ "Facturar reparaciones", 				FacturarReparacionesAction.class },
			{ "Liquidar factura", 					LiquidarFacturaAction.class },
		};
	}

	public static void main(String[] args) {
		new MainMenu().execute();
	}

}
