package uo.ri.ui.admin.action.contractType;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.List;
import java.util.Map;

/**
 * Clase que contiene la ui para listar los tipos de contratos.
 */
public class ListContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {

        Map<ContractTypeDto, Map<String, Object>> contractTypeDtos = ServiceFactory.getContractTypeCRUDService()
                .findAllContractType();
        Printer.printListContractTypes(contractTypeDtos);
    }
}
