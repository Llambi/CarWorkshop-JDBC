package uo.ri.ui.admin.action.contract;

import alb.util.console.Console;
import alb.util.console.Printer;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;

import java.util.Map;

public class ListContractAction implements Action {
    @Override
    public void execute() throws Exception {
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.dni = Console.readString("DNI del mecanico");
        Map<ContractDto, Map<String,Object>> contracts = ServiceFactory.getContractCRUDService().findAllContract(mechanicDto);

        Printer.printListContracts(mechanicDto, contracts);
    }
}
