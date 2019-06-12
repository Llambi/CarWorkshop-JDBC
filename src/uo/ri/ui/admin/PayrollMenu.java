package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;
import uo.ri.ui.admin.action.payroll.*;

public class PayrollMenu extends BaseMenu {
    public PayrollMenu() {
        menuOptions = new Object[][]{
                {"Administrador > Gestión de nominas ", null},

                {"Generar nominas", GeneratePayrollsAction.class},
                {"Listar las nominas de un mecanico", ListPayrollsAction.class},
                {"Ver detalle de una nomina", FindPayrollAction.class},
                {"Eliminar ultima nomina de un mecanico", DeleteMechanicPayrollAction.class},
                {"Eliminar ultimas nominas generadas", DeleteLastPayrollsAction.class},
        };
    }
}
