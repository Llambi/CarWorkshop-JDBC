package uo.ri.ui.admin.action.contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para listar los contratos.
 */
public class ListContractAction implements Action {
    @Override
    public void execute() throws Exception {
        Long id = Console.readLong("ID del mecanico");

        List<ContractDto> contracts = new ServiceFactory()
                .forContractCrud().findContractsByMechanicId(id);

        Map<ContractDto, Map<String, Object>> map = new HashMap<>();
        for (ContractDto c : contracts){
            Map<String, Object> aux = new HashMap<>();
            aux.put("payrolls", new ServiceFactory().forPayroll()
                    .findPayrollsByMechanicId(c.mechanicId));
            aux.put("liquidacion", c.compensation);
            map.put(c,aux);
        }

        Printer.printListContracts(id,map);

    }
}
