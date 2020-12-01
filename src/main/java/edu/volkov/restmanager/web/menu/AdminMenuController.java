package edu.volkov.restmanager.web.menu;

import edu.volkov.restmanager.model.Menu;
import edu.volkov.restmanager.repository.menu.MenuRepository;
import edu.volkov.restmanager.to.MenuTo;
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
import java.util.Collections;
import java.util.List;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;
import static edu.volkov.restmanager.util.model.MenuUtil.createTo;
import static edu.volkov.restmanager.util.model.MenuUtil.getTos;

@RequestMapping("/menus")
@Controller
public class AdminMenuController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MenuRepository menuRepo;

    public AdminMenuController(MenuRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    @PostMapping
    public String updateOrCreate(
            @RequestParam(required = false) Integer id,
            Integer restId,
            String name,
            Boolean enabled,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate,
            Model model
    ) {
        log.info("\n updateOrCreate menu:{} of restaurant:{}", id, restId);
        Menu menu = new Menu(id, name, menuDate, enabled);

        if (menu.getId() == null) {
            log.info("\n create menu fo restaurant:{}", restId);
            menuRepo.save(menu, restId);
        } else {
            log.info("\n update menu:{} of restaurant:{}", id, restId);
            checkNotFoundWithId(menuRepo.save(menu, restId), menu.getId());
        }

        model.addAttribute("restId", restId);
        return "redirect:/menus/restaurant";
    }

    @GetMapping("/update")
    public String update(Integer id, Integer restId, Model model) {
        MenuTo to = createTo(checkNotFound(menuRepo.get(id, restId), "menu by id: " + id + "dos not exist for rest:" + restId));
        model.addAttribute("menuTo", to);
        return "menuForm";
    }

    @GetMapping("/create")
    public String create(Integer restId, Model model) {
        model.addAttribute("menuTo", new MenuTo(null, "def", LocalDate.now(), false, restId, Collections.emptyList()));
        return "menuForm";
    }

    @GetMapping("/delete")
    public String delete(int id, int restId, Model model) {
        log.info("\n delete menu:{} of restaurant:{}", id, restId);
        checkNotFoundWithId(menuRepo.delete(id, restId), id);

        model.addAttribute("restId", restId);
        return "redirect:/menus/restaurant";
    }

    @GetMapping("/restaurant")
    public String getAll(Integer restId, Model model) {
        log.info("\n getAll for restaurant:{}", restId);
        List<MenuTo> tos = getTos(menuRepo.getAll(restId));

        model.addAttribute("menuTos", tos);
        log.info("\n getAll end {}", tos);
        return "menus";
    }

    @GetMapping("/filter")
    public String getBetween(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Integer restId,
            Model model) {
        log.info("\n getBetween start{}, end{} for restaurant:{}", startDate, endDate, restId);
        List<MenuTo> tos = getTos(menuRepo.getBetween(startDate, endDate, restId));

        model.addAttribute("menuTos", tos);
        return "menus";
    }
}
