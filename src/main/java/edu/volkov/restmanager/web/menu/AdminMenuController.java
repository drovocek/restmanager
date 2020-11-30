package edu.volkov.restmanager.web.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.service.MenuService;
import edu.volkov.restmanager.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/menus")
@Controller
public class AdminMenuController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MenuService service;

    public AdminMenuController(MenuService service) {
        this.service = service;
    }

    @PostMapping
    public String save(
            @RequestParam(required = false) Integer menuId,
            Integer restaurantId,
            String name,
            Boolean enabled,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate,
            Model model
    ) {
        Menu menu = new Menu(menuId, name, null, menuDate, enabled);

        if (menu.isNew()) {
            service.create(menu, restaurantId);
        } else {
            service.update(menu, restaurantId);
        }

        model.addAttribute("restaurantId", restaurantId);
        return "redirect:/menus/restaurant";
    }

    @GetMapping("/menuForm")
    public String updateOrCreate(
            @RequestParam(required = false) Integer menuId,
            @RequestParam Integer restaurantId,
            Model model) {
        Menu menu = (menuId == null)
                ? new Menu(null, "", null, LocalDate.now(), false)
                : service.get(menuId, restaurantId);
        model.addAttribute("menu", menu);
        model.addAttribute("restaurantId", restaurantId);

        return "menuForm";
    }

    @GetMapping("/delete")
    public String erase(Integer menuId, Integer restaurantId, Model model) {
        service.delete(menuId, restaurantId);
        model.addAttribute("restaurantId", restaurantId);
        return "redirect:/menus/restaurant";
    }

    @GetMapping("/restaurant")
    public String getAllByRestId(Integer restaurantId, Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAllByRestId for user {}", userId);

        LocalDate startDate = LocalDate.of(2010, 1, 1);
        LocalDate endDate = LocalDate.of(2030, 1, 1);

        List<Menu> allByRestId = service.getByRestIdBetweenDates(restaurantId, startDate, endDate);
        log.info("getAllByRestId for user {} return", allByRestId);

        model.addAttribute("menus", allByRestId);
        return "menus";
    }
}
