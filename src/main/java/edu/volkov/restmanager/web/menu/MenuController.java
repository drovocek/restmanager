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
            @RequestParam(name = "menuId", required = false) Integer menuId,
            @RequestParam(name = "restaurantId", required = false) Integer restaurantId,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate
    ) {
        Menu menu = new Menu(menuId, name, menuDate);

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
                ? new Menu("", LocalDate.now(), false)
                : service.get(id);
        model.addAttribute("restaurant", menu);
        return "menuForm";
    }

    @GetMapping("/delete")
    public String erase(@RequestParam(name = "id") Integer id) {
        service.delete(id);
        return "redirect:/menus";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("menus",service.getAll());
        return "menus";
    }

    @GetMapping("/getallbyname")
    public String getAllByName(@RequestParam(name = "name") String name, Model model) {
        model.addAttribute("menus",service.getAllByName(name));
        return "menus";
    }
}
