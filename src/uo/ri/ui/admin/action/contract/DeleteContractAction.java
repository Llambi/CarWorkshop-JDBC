package uo.ri.ui.admin.action.contract;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class DeleteContractAction implements Action {
    @Override
    public void execute() throws Exception {
        ContractDto contractDto = new ContractDto();
        contractDto.id = Console.readLong("Identificador de contrato a eliminar");

        ServiceFactory.getContractCRUDService().deleteContract(contractDto);

        Printer.printDeleteContract();

    }
}
