package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.to.MenuItemTo;

import java.util.List;
import java.util.stream.Collectors;

public class MenuItemUtil {

    public static MenuItem createNewFromTo(MenuItemTo menuItemTo) {
        return new MenuItem(null, menuItemTo.getName(), menuItemTo.getPrice());
    }

    public static List<MenuItem> createNewsFromTos(Menu menu, List<MenuItemTo> menuItemTos) {
        return menuItemTos.stream()
                .map(MenuItemUtil::createNewFromTo)
                .peek(menuItem -> menuItem.setMenu(menu))
                .collect(Collectors.toList());
    }

    public static MenuItemTo asTo(MenuItem menuItm) {
        return new MenuItemTo(
                menuItm.id(),
                menuItm.getName(),
                menuItm.getPrice()
        );
    }

    public static List<MenuItemTo> getTos(List<MenuItem> menuItems) {
        return menuItems.stream()
                .map(MenuItemUtil::asTo)
                .collect(Collectors.toList());
    }
}




