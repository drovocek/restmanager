package edu.volkov.restmanager.web.jsp.menuitem;

import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.to.MenuItemTo;
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

@RequestMapping("/admin/menuItems")
@Controller
public class JspAdminMenuItemController {

    private final Logger log = LoggerFactory.getLogger(getClass());
//    private final MenuItemRepository repository;
//
//    public JspAdminMenuItemController(MenuItemRepository menuItmRepo) {
//        this.repository = menuItmRepo;
//    }
//
//    @PostMapping
//    public String save(
//            @RequestParam(required = false) Integer id,
//            Integer menuId,
//            String name,
//            Integer price,
//            Model model
//    ) {
//        MenuItem menuItm = new MenuItem(id, name, price);
//
//        if (menuItm.isNew()) {
//            log.info("\n create menuItm for menu:{}", menuId);
//            repository.save(menuItm, menuId);
//        } else {
//            log.info("\n update menuItm:{} of menu:{}", id, menuId);
//            checkNotFoundWithId(repository.save(menuItm, menuId), menuItm.getId());
//        }
//
//        model.addAttribute("menuItm", menuItm);
//        return "redirect:/menus/restaurant";
//    }
//
//    @GetMapping("/menuItemForm")
//    public String updateOrCreate(
//            @RequestParam(required = false) Integer id,
//            String name,
//            Integer price,
//            Integer menuId,
//            Model model
//    ) {
//        log.info("\n updateOrCreate menuItem:{} of menu:{}", id, menuId);
//        MenuItem menuItm = new MenuItem(id, name, price);
//
//        if (menuItm.getId() == null) {
//            log.info("\n create menuItem fo menu:{}", menuId);
//            repository.save(menuItm, menuId);
//        } else {
//            log.info("\n update menuItem:{} of menu:{}", id, menuId);
//            checkNotFoundWithId(repository.save(menuItm, menuId), menuItm.getId());
//        }
//
//        model.addAttribute("menuId", menuId);
//        return "menuItemForm";
//    }
//
//    @GetMapping("/update")
//    public String update(Integer id, Integer menuId, Model model) {
//        MenuItemTo to = createTo(checkNotFound(repository.get(id, menuId), "menuItem by id: " + id + "dos not exist for menu:" + menuId));
//        model.addAttribute("menuItmTo", to);
//        return "menuItemForm";
//    }
//
//    @GetMapping("/create")
//    public String create(Integer menuId, Model model) {
//        model.addAttribute("menuItmTo", new MenuItemTo(null, "def", 0, menuId));
//        return "menuItemForm";
//    }
//
//    @GetMapping("/delete")
//    public String delete(int menuItmId, int menuId, Model model) {
//        log.info("\n delete menuItm:{} of menu:{}", menuItmId, menuId);
//        checkNotFoundWithId(repository.delete(menuItmId, menuId), menuItmId);
//
//        model.addAttribute("restId", menuId);
//        return "redirect:/menus/restaurant";
//    }
}
