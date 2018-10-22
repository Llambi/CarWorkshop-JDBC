package uo.ri.ui.admin.action.contract;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.Map;

public class ListContractAction implements Action {
    @Override
    public void execute() throws Exception {
        //TODO: Metodo que liste los contratos de un mecanico dado con las specificaciones requeridas.
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.dni = Console.readString("DNI del mecanico");
        Map<ContractDto, Map<String,Object>> contracts = ServiceFactory.getContractCRUDService().findAllContract(mechanicDto);

        Printer.printListContracts(mechanicDto, contracts);
    }
}
