package uo.ri.ui.admin.action.contract;

import alb.util.console.Console;
import alb.util.date.Dates;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para actualizar un contrato.
 */
public class UpdateContractAction implements Action {
    @Override
    public void execute() throws Exception {
        ContractDto contractDto = new ContractDto();
        contractDto.id = Console
                .readLong("Identificador del contrato a actualizar");
        contractDto.endDate = Dates.fromString(Console
                .readString("Nuevo fin de contrato (Formato dd-mm-aaaa"));
        contractDto.yearBaseSalary = Console.readDouble("Nuevo salario base por año");

        new ServiceFactory().forContractCrud().updateContract(contractDto);

        Printer.printUpdateContract();
    }
}
