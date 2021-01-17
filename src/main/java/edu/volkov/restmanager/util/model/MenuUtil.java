package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.to.MenuTo;

public class MenuUtil {

    public static Menu updateFromTo(Menu menu, MenuTo menuTo) {
        menu.setName(menuTo.getName());
        menu.setMenuDate(menuTo.getMenuDate());
        menu.setEnabled(menuTo.isEnabled());
        menu.setMenuItems(
                MenuItemUtil.createNewsFromTos(menu, menuTo.getMenuItemTos())
        );
        return menu;
    }

    public static MenuTo asTo(Menu menu) {
        return new MenuTo(
                menu.getId(),
                menu.getName(),
                menu.getMenuDate(),
                menu.isEnabled(),
                MenuItemUtil.getTos(menu.getMenuItems())
        );
    }
}




