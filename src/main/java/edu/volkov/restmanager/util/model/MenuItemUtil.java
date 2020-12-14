package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.HasId;
import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.model.Role;
import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.to.MenuItemTo;
import edu.volkov.restmanager.to.UserTo;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuItemUtil {

    public static MenuItem createNewFromTo(MenuItemTo menuItemTo) {
        return new MenuItem(null,menuItemTo.getName(),menuItemTo.getPrice());
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

    public static void updateAllFromTo(List<MenuItem> updatedMenuItems, List<MenuItemTo> menuItemTos) {
        Map<Integer, MenuItemTo> tosById = menuItemTos.stream()
                .collect(Collectors.toMap(HasId::id, menuItemTo -> menuItemTo));
        System.out.println("!!!menuItemTos!!!!! " + menuItemTos);
        System.out.println("!!!updatedMenuItems!!!!! " + updatedMenuItems);
        updatedMenuItems.forEach(
                menuItem -> {
                    MenuItemTo menuTo = tosById.get(menuItem.id());
                    if (menuTo != null) {
                        menuItem.setName(tosById.get(menuItem.id()).getName());
                        menuItem.setPrice(tosById.get(menuItem.id()).getPrice());
                    }
                }
        );

        System.out.println("!!!!!! " + updatedMenuItems);
    }
}




