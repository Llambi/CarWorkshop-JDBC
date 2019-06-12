package uo.ri.ui.admin.action.category;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class DeleteCategoryAction implements Action {
    @Override
    public void execute() throws Exception {

        // Pedir datos
        long id= Console.readLong("Id de la categoria:");

        new ServiceFactory().forContractCategoryCrud().deleteContractCategory(id);

        // Mostrar resultado
        Printer.printDeleteCategory();
    }}