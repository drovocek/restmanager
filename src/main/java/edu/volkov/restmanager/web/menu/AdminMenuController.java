package edu.volkov.restmanager.web.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.repository.menu.MenuRepository;
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

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@RequestMapping("/menus")
@Controller
public class AdminMenuController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MenuRepository menuRepo;

    public AdminMenuController(MenuRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    @PostMapping
    public String save(
            @RequestParam(required = false) Integer id,
            Integer restId,
            String name,
            Boolean enabled,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate,
            Model model
    ) {
        Menu menu = new Menu(id, name, null, menuDate, enabled);

        if (menu.isNew()) {
            log.info("create menu fo restaurant:{}", restId);
            menuRepo.save(menu, restId);
        } else {
            log.info("update menu:{} of restaurant:{}", id, restId);
            checkNotFoundWithId(menuRepo.save(menu, restId), menu.getId());
        }

        model.addAttribute("restId", restId);
        return "redirect:/menus/restaurant";
    }

    @GetMapping("/menuForm")
    public String updateOrCreate(
            @RequestParam(required = false) Integer id,
            @RequestParam Integer restId,
            Model model) {
        log.info("updateOrCreate menu:{} of restaurant:{}", id, restId);
        Menu menu = (id == null)
                ? new Menu(null, "", null, LocalDate.now(), false)
                : checkNotFound(menuRepo.get(id, restId),
                "menu by id: " + id + "dos not exist for rest:" + restId);
        model.addAttribute("menu", menu);
        model.addAttribute("restId", restId);

        return "menuForm";
    }

    @GetMapping("/delete")
    public String erase(Integer id, Integer restId, Model model) {
        log.info("erase menu:{} of restaurant:{}", id, restId);
        checkNotFoundWithId(menuRepo.delete(id, restId), (int) id);

        model.addAttribute("restId", restId);
        return "redirect:/menus/restaurant";
    }

    @GetMapping("/restaurant")
    public String getAll(Integer restId, Model model) {
        log.info("getAll for restaurant:{}", restId);
        List<Menu> allByRestId = menuRepo.getAll(restId);

        model.addAttribute("menus", allByRestId);
        return "menus";
    }

    @GetMapping("/filter")
    public String getBetween(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Integer restId,
            Model model) {
        log.info("getBetween start{}, end{} for restaurant:{}", startDate, endDate, restId);
        List<Menu> allByRestId = menuRepo.getBetween(startDate, endDate, restId);

        model.addAttribute("menus", allByRestId);
        return "menus";
    }
}
