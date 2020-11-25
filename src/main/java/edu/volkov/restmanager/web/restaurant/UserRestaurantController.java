package edu.volkov.restmanager.web.restaurant;

import edu.volkov.restmanager.service.RestaurantService;
import edu.volkov.restmanager.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static edu.volkov.restmanager.util.RestaurantUtil.getTo;

import java.time.LocalDate;

@RequestMapping("/restaurants")
@Controller
public class UserRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantService service;

    public UserRestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public String getAllWithPreassignedQuantityEnabledMenu(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAllWithMenu for user {}", userId);

        model.addAttribute(
                "restaurants",
                getTo(service.getAllWithPreassignedQuantityEnabledMenu()));
        return "restaurants";
    }

//    @GetMapping("/filter")
//    public String getFilteredWithEnabledMenu(
//            @RequestParam(required = false) String restaurantName,
//            @RequestParam(required = false) String restaurantAddress,
//            Model model
//    ) {
//        int userId = SecurityUtil.authUserId();
//        log.info("getAllWithFiltered for user {}", userId);
//
//        model.addAttribute(
//                "restaurants",
//                getTo(service.getFilteredWithEnabledMenu(restaurantName, restaurantAddress))
//        );
//        return "restaurants";
//    }

    @GetMapping("/vote")
    public String vote(
            @RequestParam(name = "id") Integer id
    ) {
        service.vote(SecurityUtil.authUserId(), id, LocalDate.now());
        return "redirect:/restaurants";
    }
}
