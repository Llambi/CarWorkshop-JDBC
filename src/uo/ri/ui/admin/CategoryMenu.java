package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;
import uo.ri.ui.admin.action.category.AddCategoryAction;
import uo.ri.ui.admin.action.category.DeleteCategoryAction;
import uo.ri.ui.admin.action.category.ListCategoryAction;
import uo.ri.ui.admin.action.category.UpdateCategoryAction;
import uo.ri.ui.admin.action.contract.*;

public class CategoryMenu extends BaseMenu {

    public CategoryMenu() {
        menuOptions = new Object[][]{
                {"Administrador > Gestión de categorias ", null},

                {"Añadir categoria", AddCategoryAction.class},
                {"Modificar una categoria", UpdateCategoryAction.class},
                {"Eliminar una categoria", DeleteCategoryAction.class},
                {"Listar categorias", ListCategoryAction.class}
        };
    }
}
