package uo.ri.ui.admin.action.contractType;

import alb.util.menu.Action;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.List;
import java.util.Map;

public class ListContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {

        Map<ContractTypeDto, List<MechanicDto>> contractTypeDtos = ServiceFactory.getContractTypeCRUDService().findAllContractType();

        // Mostrar resultado
        Printer.printContratTypes(contractTypeDtos);
    }
}
