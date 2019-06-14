package uo.ri.ui.admin.action.contract;

import alb.util.console.Console;
import alb.util.date.Dates;
import alb.util.menu.Action;
import uo.ri.business.ContractCrudService;
import uo.ri.business.dto.ContractDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para terminar un contrato.
 */
public class TerminateContractAction implements Action {

    @Override
    public void execute() throws Exception {
        ContractDto contractDto = new ContractDto();

        contractDto.id = Console.readLong("Identificador del contrato a extinguir");
        contractDto.endDate = Dates.fromString(Console
                .readString("Fecha de extincion del contrato (Formato dd-mm-aaaa)"));

        ContractCrudService contractCrudService = new ServiceFactory().forContractCrud();

        contractCrudService.finishContract(contractDto.id,contractDto.endDate);
        ContractDto c = contractCrudService.findContractById(contractDto.id);


        Printer.printTerminateContract(c);
    }
}
