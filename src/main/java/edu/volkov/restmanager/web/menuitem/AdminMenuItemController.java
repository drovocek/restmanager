package edu.volkov.restmanager.web.menuitem;

import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menuItem.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;

@RequestMapping("/menuItems")
@Controller
public class AdminMenuItemController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MenuItemRepository menuItmRepo;

    public AdminMenuItemController(MenuItemRepository menuItmRepo) {
        this.menuItmRepo = menuItmRepo;
    }

    @PostMapping
    public String save(
            @RequestParam(required = false) Integer id,
            Integer menuId,
            String name,
            Boolean enabled,
            Integer price,
            Model model
    ) {
        MenuItem menuItm = new MenuItem(id, name, enabled, price);

        if (menuItm.isNew()) {
            log.info("\n create menuItm for menu:{}", menuId);
            menuItmRepo.save(menuItm, menuId);
        } else {
            log.info("\n update menuItm:{} of menu:{}", id, menuId);
            checkNotFoundWithId(menuItmRepo.save(menuItm, menuId), menuItm.getId());
        }

        model.addAttribute("menuItm", menuItm);
        return "redirect:/menus/restaurant";
    }

    @GetMapping("/menuItemForm")
    public String updateOrCreate(
            @RequestParam(required = false) Integer id,
            @RequestParam Integer menuId,
            Model model) {
        log.info("\n updateOrCreate menu:{} of restaurant:{}", id, menuId);
        MenuItem menuItm = (id == null)
                ? new MenuItem(null, "", false, null)
                : checkNotFound(menuItmRepo.get(id, menuId),
                "menuItm by id: " + id + "dos not exist for menu:" + menuId);
        model.addAttribute("menu", menuItm);
        model.addAttribute("restId", menuId);

        return "menuForm";
    }

    @GetMapping("/delete")
    public String delete(int menuItmId, int menuId, Model model) {
        log.info("\n delete menuItm:{} of menu:{}", menuItmId, menuId);
        checkNotFoundWithId(menuItmRepo.delete(menuItmId, menuId), menuItmId);

        model.addAttribute("restId", menuId);
        return "redirect:/menus/restaurant";
    }
}
