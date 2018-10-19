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

        Map<ContractTypeDto, List<MechanicDto>> contractTypeDtos = ServiceFactory.getContractTypeCRUDService().findAllContractType();

        // Mostrar resultado
        //TODO: Imprimir Tipo de contrato:->n*Trabajadores... Acumulado salario + numeroTotalTrabajadores

        for (Map.Entry<ContractTypeDto, List<MechanicDto>> entry : contractTypeDtos.entrySet()) {
            ContractTypeDto contractTypeDto = entry.getKey();
            Console.printf("Los siguientes mecanicos tiene el contrato %s:\n\n", contractTypeDto.name);
            for (MechanicDto mechanicDto : entry.getValue()) {
                Console.printf("%s %s\tDNI: %s\n", mechanicDto.name, mechanicDto.surname, mechanicDto.dni);
            }
            Double totalBaseSalary = ServiceFactory.getContractTypeCRUDService().getTotalBaseSalary(contractTypeDto);
            //TODO: Sueldo acumulado de los empleados.
            Console.printf("El numero total de mecanicos con este contrato son: %s",entry.getValue().size());
        }
    }
}
