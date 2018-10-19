package uo.ri.ui.admin.action.contractType;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;

import java.util.List;
import java.util.Map;

public class ListContractTypeAction implements Action {
    @Override
    public void execute() throws Exception {

        Map<ContractTypeDto, Map<String, Object>> contractTypeDtos = ServiceFactory.getContractTypeCRUDService().findAllContractType();

        for (Map.Entry<ContractTypeDto, Map<String, Object>> entry : contractTypeDtos.entrySet()) {
            ContractTypeDto contractTypeDto = entry.getKey();
            @SuppressWarnings("unchecked")
            List<MechanicDto> mechanics = (List<MechanicDto>) entry.getValue().get("mechanic");
            double acumSalary = (double) entry.getValue().get("acumSalary");

            Console.printf("Los siguientes mecanicos tiene el contrato %s:\n\n", contractTypeDto.name);
            mechanics.forEach(mechanicDto -> Console.printf("%s %s\tDNI: %s\n", mechanicDto.name, mechanicDto.surname, mechanicDto.dni));
            Console.printf("El numero total de mecanicos con este tipo de contrato son: %s", mechanics.size());
            Console.printf("El salaruio acumulado de mecanicos con este tipo de contrato son: %s", acumSalary);
        }
    }
}
