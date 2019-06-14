package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;
import uo.ri.ui.admin.action.contractType.AddContractTypeAction;
import uo.ri.ui.admin.action.contractType.DeleteContractTypeAction;
import uo.ri.ui.admin.action.contractType.ListContractTypeAction;
import uo.ri.ui.admin.action.contractType.UpdateContractTypeAction;

public class ContractTypeMenu extends BaseMenu {

    public ContractTypeMenu() {
        menuOptions = new Object[][]{
                {"Administrador > Gestión de tipos de contrato", null},

                {"Añadir tipo de contrato", AddContractTypeAction.class},
                {"Modificar datos de tipo de contrato", UpdateContractTypeAction.class},
                {"Eliminar tipo de contrato", DeleteContractTypeAction.class},
                {"Listar trabajadores por tipo de contrato", ListContractTypeAction.class}
        };
    }
}
