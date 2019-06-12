package uo.ri.ui.admin.action.category;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class UpdateCategoryAction implements Action {

    @Override
    public void execute() throws Exception {
        ContractCategoryDto contractCategoryDto = new ContractCategoryDto();

        // Pedir datos
        contractCategoryDto.name = Console.readString("Nombre");
        contractCategoryDto.trieniumSalary = Console.readInt("Importe de trienios");
        contractCategoryDto.productivityPlus = Console.readInt("Porcentaje de plus de productividad (Sin \"%\")");

        new ServiceFactory().forContractCategoryCrud().updateContractCategory(contractCategoryDto);

        // Mostrar resultado
        Printer.printUpdateCategory();
    }
}

