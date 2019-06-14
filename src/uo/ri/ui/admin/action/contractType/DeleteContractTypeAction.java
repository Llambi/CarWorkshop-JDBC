package uo.ri.ui.admin.action.contractType;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para elimiar un tipo de contrato.
 */
public class DeleteContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {

        // Pedir datos
        Long id = Console.readLong("Id del tipo de contrato");

        new ServiceFactory().forContractTypeCrud().deleteContractType(id);

        // Mostrar resultado
        Printer.printDeleteTypeContract();
    }
}
