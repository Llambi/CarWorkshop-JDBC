package uo.ri.ui.admin.action.contractType;

import alb.util.menu.Action;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.impl.ContractTypeCRUDImpl;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.List;

public class ListContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {

        List<ContractTypeDto> contractTypeDtos = ServiceFactory.getContractTypeCRUDService().findAllContractType();

        // Mostrar resultado
        Printer.printContratTypes(contractTypeDtos);
    }
}
