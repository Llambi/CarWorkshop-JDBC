package uo.ri.ui.admin.action.contractType;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.ServiceFactory;

/**
 * Clase que contiene la ui para elimiar un tipo de contrato.
 */
public class DeleteContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {
        ContractTypeDto contractTypeDto = new ContractTypeDto();

        // Pedir datos
        contractTypeDto.name = Console.readString("Nombre del tipo de contrato");

        ServiceFactory.getContractTypeCRUDService().deleteContractType(contractTypeDto);

        // Mostrar resultado
        Console.println("Tipo de contrato eliminado");
    }
}
