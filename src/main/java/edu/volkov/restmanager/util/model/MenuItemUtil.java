package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.HasId;
import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.to.MenuItemTo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuItemUtil {
    public static MenuItemTo createTo(MenuItem menuItm) {
        return new MenuItemTo(
                menuItm.id(),
                menuItm.getName(),
                menuItm.getPrice(),
                menuItm.getMenu().id()
        );
    }

    public static List<MenuItemTo> getTos(List<MenuItem> rests) {
        return rests.stream()
                .map(MenuItemUtil::createTo)
                .collect(Collectors.toList());
    }

    public static void updateAllFromTo(List<MenuItem> updatedMenuItems, List<MenuItemTo> menuItemTos) {
        Map<Integer, MenuItemTo> tosById = menuItemTos.stream()
                .collect(Collectors.toMap(HasId::id, menuItemTo -> menuItemTo));

        updatedMenuItems.forEach(
                menuItem -> {
                    MenuItemTo menuTo = tosById.get(menuItem.id());
                    if (menuTo != null) {
                        menuItem.setName(tosById.get(menuItem.id()).getName());
                        menuItem.setPrice(tosById.get(menuItem.id()).getPrice());
                    }
                }
        );
    }
}




