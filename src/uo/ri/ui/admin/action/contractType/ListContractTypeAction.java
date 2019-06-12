package uo.ri.ui.admin.action.contractType;

import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.persistence.impl.ContracStatus;
import uo.ri.ui.util.Printer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Clase que contiene la ui para listar los tipos de contratos.
 */
public class ListContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {

        List<MechanicDto> mechanicDtos = new ServiceFactory().forMechanicCrudService().findActiveMechanics();
        List<ContractTypeDto> contractTypeDtos = new ServiceFactory().forContractTypeCrud().findAllContractTypes();

        Map<ContractTypeDto, Map<MechanicDto, ContractDto>> map = new HashMap<>();
        Map<MechanicDto, ContractDto> activeContractDtos = new HashMap<>();


        for (MechanicDto m : mechanicDtos) {
            ContractDto activeContract= new ServiceFactory().forContractCrud().findContractsByMechanicId(m.id).stream()
                    .filter(contractDto -> contractDto.status.equalsIgnoreCase(ContracStatus.ACTIVE.toString())).findFirst().orElse(null);
            activeContractDtos.put(m,activeContract);
        }
        for (ContractTypeDto ct : contractTypeDtos){
            for (Map.Entry<MechanicDto, ContractDto> entry: activeContractDtos.entrySet()){
                if(ct.name.equalsIgnoreCase(entry.getValue().typeName)){
                    map.put(ct, (Map<MechanicDto, ContractDto>) entry);
                }
            }
        }

        Printer.printListContractTypes(map);
    }
}
