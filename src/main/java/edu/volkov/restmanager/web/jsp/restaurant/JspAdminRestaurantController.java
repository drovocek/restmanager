package edu.volkov.restmanager.web.jsp.restaurant;

import edu.volkov.restmanager.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.function.Predicate;

import static edu.volkov.restmanager.util.ValidationUtil.checkNotFound;
import static edu.volkov.restmanager.util.ValidationUtil.checkNotFoundWithId;
import static edu.volkov.restmanager.util.model.RestaurantUtil.filtrate;


@RequestMapping("/admin/restaurants")
@Controller
public class JspAdminRestaurantController {

//    private final Logger log = LoggerFactory.getLogger(getClass());
//    private final RestaurantRepository repository;
//
//    public JspAdminRestaurantController(RestaurantRepository repository) {
//        this.repository = repository;
//    }
//
//    @PostMapping
//    public String save(
//            @RequestParam(required = false) Integer id,
//            String name,
//            String address,
//            String phone,
//            Boolean enabled
//    ) {
//        Restaurant restaurant = new Restaurant(id, name, address, phone, enabled, 0);
//
//        if (restaurant.isNew()) {
//            log.info("\n create restaurant");
//            repository.save(restaurant);
//        } else {
//            log.info("\n update restaurant");
//            checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
//        }
//
//        return "redirect:/admin/restaurants";
//    }
//
//    @GetMapping("/update")
//    public String update(Integer id, Model model) {
//        Restaurant rest = checkNotFound(repository.getWithoutMenu(id), "restaurant by id: " + id + "dos not exist");
//        model.addAttribute("restTo", rest);
//        return "restaurantForm";
//    }
//
//    @GetMapping("/create")
//    public String create(Model model) {
//        Restaurant toDef = new Restaurant(null, "def", "", "+7(495) 000-0000", false, 0);
//        model.addAttribute("restTo", toDef);
//        return "restaurantForm";
//    }
//
//    @GetMapping("/delete")
//    public String erase(int id) {
//        log.info("\n erase for restaurant {}", id);
//        checkNotFoundWithId(repository.delete(id), id);
//        return "redirect:/admin/restaurants";
//    }
//
//    @GetMapping
//    public String getAll(Model model) {
//        log.info("\n getAll restaurants");
//        Predicate<Restaurant> filter = rest -> true;
//        List<Restaurant> tos = filtrate(repository.getAllWithDayEnabledMenu(), filter);
//
//        model.addAttribute("restaurants", tos);
//        return "restaurantsManaging";
//    }
}
