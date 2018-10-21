package uo.ri.ui.admin;

import uo.ri.ui.admin.action.mechanic.AddMechanicAction;
import uo.ri.ui.admin.action.mechanic.DeleteMechanicAction;
import uo.ri.ui.admin.action.mechanic.ListMechanicsAction;
import uo.ri.ui.admin.action.mechanic.UpdateMechanicAction;
import alb.util.menu.BaseMenu;

public class MecanicosMenu extends BaseMenu {

	public MecanicosMenu() {
		menuOptions = new Object[][] { 
			{"Administrador > Gestión de mecánicos", null},
			
			{ "Añadir mecánico", 				AddMechanicAction.class }, 
			{ "Modificar datos de mecánico", 	UpdateMechanicAction.class }, 
			{ "Eliminar mecánico", 				DeleteMechanicAction.class }, 
			{ "Listar mecánicos", 				ListMechanicsAction.class },
		};
	}

}
