package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.to.MenuItemTo;

import java.util.List;
import java.util.stream.Collectors;

public class MenuItemUtil {
    public static MenuItemTo createTo(MenuItem menuItm) {
        return new MenuItemTo(
                menuItm.id(),
                menuItm.getName(),
                menuItm.getPrice(),
                menuItm.isEnabled(),
                menuItm.getMenu().getId()
        );
    }

    public static List<MenuItemTo> getTos(List<MenuItem> rests) {
        return rests.stream()
                .map(MenuItemUtil::createTo)
                .collect(Collectors.toList());
    }
}




