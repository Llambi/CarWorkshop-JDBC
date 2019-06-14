package uo.ri.ui.admin.action.contract;

import alb.util.console.Console;
import alb.util.console.Printer;
import alb.util.date.Dates;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.conf.ServiceFactory;

/**
 * Clase que contiene la ui para añadir un contrato.
 */
public class AddContractAction implements Action {
    @Override
    public void execute() throws Exception {
        ContractDto contractDto = new ContractDto();
        contractDto.mechanicId = Console.readLong("ID del mecanico a contratar");

        contractDto.typeId = Console.readLong("ID de tipo de contrato");

        contractDto.categoryId = Console.readLong("ID de categoria del mecanico");

        String initMonthYear;
        boolean flag = false;
        do {

            try {
                initMonthYear = Console
                        .readString("Fecha de inicio de contrato (Formato dd-mm-aaaa");
                contractDto.startDate = Dates.fromString(initMonthYear);
                flag = true;
            } catch (ArrayIndexOutOfBoundsException ignored) {
                Printer.print("Error en el formato de fecha");
            }
        } while (!flag);

        contractDto.yearBaseSalary = Console
                .readDouble("Salario base anual");

        String question = Console
                .readString("Quiere registrar fecha de fin de contrato? Si o No");
        if ("S".equalsIgnoreCase(question) || "SI".equalsIgnoreCase(question)) {
            contractDto.endDate = Dates.fromString(
                    Console.readString("Fecha de fin " +
                            "de contrato (Formato dd-mm-aaaa"));
        }

        new ServiceFactory().forContractCrud()
                .addContract(contractDto);

        Console.println("Contrato añadido");
    }
}
