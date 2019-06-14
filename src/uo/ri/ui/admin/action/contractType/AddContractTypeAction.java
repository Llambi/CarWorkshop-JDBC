package uo.ri.ui.admin.action.contractType;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para añadir un tipo de contrato.
 */
public class AddContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {
        ContractTypeDto contractTypeDto = new ContractTypeDto();

        // Pedir datos
        contractTypeDto.name = Console.readString("Nombre");
        contractTypeDto.compensationDays = Console
                .readInt("Dias de compensacion");

        new ServiceFactory().forContractTypeCrud()
                .addContractType(contractTypeDto);

        // Mostrar resultado
        Printer.printAddTypeContract();
    }
}
