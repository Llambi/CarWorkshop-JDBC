package uo.ri.ui.admin.action.contract;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para eliminar un contrato.
 */
public class DeleteContractAction implements Action {
    @Override
    public void execute() throws Exception {
        Long id;
        id = Console.readLong("Identificador de contrato a eliminar");

        new ServiceFactory().forContractCrud().deleteContract(id);

        Printer.printDeleteContract();

    }
}
