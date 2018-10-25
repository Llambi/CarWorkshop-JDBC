package uo.ri.ui.admin.action.contract;

import alb.util.console.Console;
import alb.util.date.Dates;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

import java.util.Map;

public class AddContractAction implements Action {
    @Override
    public void execute() throws Exception {
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.dni = Console.readString("DNI del mecanico a contratar");

        ContractTypeDto contractTypeDto = new ContractTypeDto();
        contractTypeDto.name = Console.readString("Tipo de contrato");

        ContractCategoryDto contractCategoryDto = new ContractCategoryDto();
        contractCategoryDto.name = Console.readString("Categoria del mecanico");

        ContractDto contractDto = new ContractDto();
        String initMonthYear = Console.readString("Mes y año de inicio de contrato (Formato numerico mm-aaaa");
        contractDto.startDate = Dates.fromString("1-" + initMonthYear);
        contractDto.yearBaseSalary = Console.readDouble("Salario base anual");
        String endContractString = Console.readString("Fecha fin del contrato (Formato dd-mm-aaa) si no se desea, escribir no");
        if (!endContractString.equalsIgnoreCase("no"))
            contractDto.endDate = endContractString.equals("") ? null : Dates.fromString(endContractString);

        Map<String, Object> liquidacion = ServiceFactory.getContractCRUDService().addContract(mechanicDto, contractTypeDto, contractCategoryDto, contractDto);
        Printer.printLiquidacion(liquidacion);

        Console.println("Contrato añadido");
    }
}
