package uo.ri.ui.admin.action.contractType;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para actualizar un tipo de contrato.
 */
public class UpdateContractTypeAction implements Action {

    @Override
    public void execute() throws Exception {

        ContractTypeDto contractTypeDto = new ContractTypeDto();
        contractTypeDto.name = Console.readString("Nombre de tipo de contrato");
        contractTypeDto.compensationDays = Console.readInt("Nuevos dias de compensacion");

        new ServiceFactory().forContractTypeCrud().updateContractType(contractTypeDto);

        // Mostrar resultado
        Printer.printUpdateTypeContract();
    }


}
