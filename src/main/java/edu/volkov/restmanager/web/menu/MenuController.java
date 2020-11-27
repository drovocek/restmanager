package edu.volkov.restmanager.web.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.service.MenuService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@RequestMapping("/menus")
@Controller
public class MenuController {
    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @PostMapping
    public String save(
            @RequestParam(required = false) Integer menuId,
            @RequestParam(required = false) Integer restaurantId,
            @RequestParam String name,
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate
    ) {
        Menu menu = new Menu(menuId, name, null, menuDate, false);

        System.out.println(menu);
        if (menu.isNew()) {
            service.create(menu, restaurantId);
        } else {
            service.update(menu, restaurantId);
        }

        return "redirect:/menus";
    }

    @GetMapping("/menuForm")
    public String getRestaurantForm(
            @RequestParam(name = "id", required = false) Integer id,
            Model model
    ) {
        Menu menu = (id == null)
                ? new Menu(null, "", null, LocalDate.now(), false)
                : service.get(id);
        model.addAttribute("menu", menu);

        return "menuForm";
    }

    @GetMapping("/delete")
    public String erase(@RequestParam(name = "id") Integer id) {
        service.delete(id);
        return "redirect:/menus";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("menus", service.getAll());
        return "menus";
    }
}
