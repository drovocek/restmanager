package edu.volkov.restmanager.util.model;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.to.MenuTo;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MenuUtil {
    public static MenuTo createTo(Menu menu) {
        return new MenuTo(
                menu.id(),
                menu.getName(),
                menu.getMenuDate(),
                menu.isEnabled(),
                menu.getRestaurant().getId(),
                MenuItemUtil.getTos(menu.getMenuItems())
        );
    }

    public static List<MenuTo> getTos(List<Menu> menus) {
        return menus.stream()
                .map(MenuUtil::createTo)
                .collect(Collectors.toList());
    }

    public static List<Menu> filtrate(List<Menu> rests, Predicate<Menu> filter) {
        return rests.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
}




