package uo.ri.ui.admin.action.contractType;

import java.util.HashMap;import java.util.List;
import java.util.Map;

import alb.util.menu.Action;
import uo.ri.business.dto.ContracStatus;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

/**
 * Clase que contiene la ui para listar los tipos de contratos.
 */
public class ListContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {

        List<MechanicDto> mechanicDtos = new ServiceFactory()
                .forMechanicCrudService().findActiveMechanics();
        List<ContractTypeDto> contractTypeDtos =
                new ServiceFactory().forContractTypeCrud().findAllContractTypes();

        Map<ContractTypeDto, Map<MechanicDto, ContractDto>> map = new HashMap<>();
        Map<MechanicDto, ContractDto> activeContractDtos = new HashMap<>();


        for (MechanicDto m : mechanicDtos) {
            List<ContractDto> activeContract =
                    new ServiceFactory().forContractCrud()
                            .findContractsByMechanicId(m.id);

            ContractDto c = activeContract.stream()
                    .filter(contractDto -> contractDto.status
                            .equalsIgnoreCase(ContracStatus.ACTIVE.toString()))
                    .findFirst().get();

            activeContractDtos.put(m, c);
        }
        for (ContractTypeDto ct : contractTypeDtos) {
            for (Map.Entry<MechanicDto, ContractDto> entry :
                    activeContractDtos.entrySet()) {
                if (ct.name.equalsIgnoreCase(entry.getValue().typeName)) {
                    Map<MechanicDto, ContractDto> aux = new HashMap<>();
                    aux.put(entry.getKey(),entry.getValue());
                    map.put(ct, aux);
                }
            }
        }

        Printer.printListContractTypes(map);
    }
}
