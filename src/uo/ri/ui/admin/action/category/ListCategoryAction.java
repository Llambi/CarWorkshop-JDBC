package uo.ri.ui.admin.action.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.menu.Action;
import uo.ri.business.dto.ContracStatus;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class ListCategoryAction implements Action {
    @Override
    public void execute() throws Exception {

        List<MechanicDto> mechanicDtos = new ServiceFactory()
                .forMechanicCrudService().findActiveMechanics();
        List<ContractCategoryDto> categoryDtos= new ServiceFactory()
                .forContractCategoryCrud().findAllContractCategories();

        Map<ContractCategoryDto, Map<MechanicDto, ContractDto>> map =
                new HashMap<>();
        Map<MechanicDto, ContractDto> activeContractDtos = new HashMap<>();


        for (MechanicDto m : mechanicDtos) {
            ContractDto activeContract= new ServiceFactory()
                    .forContractCrud().findContractsByMechanicId(m.id).stream()
                    .filter(contractDto -> contractDto.status
                            .equalsIgnoreCase(ContracStatus.ACTIVE.toString()))
                    .findFirst().orElse(null);
            activeContractDtos.put(m,activeContract);
        }

        for (ContractCategoryDto ct : categoryDtos){
            for (Map.Entry<MechanicDto, ContractDto> entry:
                    activeContractDtos.entrySet()){
                if(ct.name.equalsIgnoreCase(entry.getValue().categoryName)){
                    Map<MechanicDto, ContractDto> aux = new HashMap<>();
                    aux.put(entry.getKey(),entry.getValue());
                    map.put(ct, aux);
                }
            }
        }

        Printer.printListCategories(map);
    }
}
