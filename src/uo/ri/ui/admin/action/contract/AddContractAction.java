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

/**
 * Clase que contiene la ui para añadir un contrato.
 */
public class AddContractAction implements Action {
    @Override
    public void execute() throws Exception {
        ContractDto contractDto = new ContractDto();
        contractDto.dni = Console.readString("DNI del mecanico a contratar");

        contractDto.typeName= Console.readString("Nombre de tipo de contrato");

        contractDto.categoryName = Console.readString("Nombre de categoria del mecanico");

        String initMonthYear = Console.readString("Mes y año de inicio de contrato (Formato numerico mm-aaaa");
        contractDto.startDate = Dates.fromString("1-" + initMonthYear);

        contractDto.yearBaseSalary = Console.readDouble("Salario base anual");

        String endContractString = Console.readString("Para no registrar fecha de dinde contrato pulse enter, en caso dontrario indiquela (Formato dd-mm-aaa)");
        contractDto.endDate = endContractString.equals("") ? null : Dates.fromString(endContractString);

        new ServiceFactory().forContractCrud()
                .addContract(contractDto);

        Console.println("Contrato añadido");
    }
}
