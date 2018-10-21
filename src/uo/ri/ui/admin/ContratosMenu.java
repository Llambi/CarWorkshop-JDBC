package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;
import uo.ri.ui.admin.action.contract.AddContractAction;
import uo.ri.ui.admin.action.contract.DeleteContractAction;
import uo.ri.ui.admin.action.contract.ListContractAction;
import uo.ri.ui.admin.action.contract.UpdateContractAction;

public class ContratosMenu extends BaseMenu {

    public ContratosMenu() {
        menuOptions = new Object[][]{
                {"Administrador > Gestión de contrato", null},

                {"Añadir contrato a un mecanico", AddContractAction.class},
                {"Modificar datos del contrato de un mecanico", UpdateContractAction.class},
                {"Eliminar contrato de un mecanico", DeleteContractAction.class},
                {"Listar contratos de un mecanico", ListContractAction.class}
        };
    }
}