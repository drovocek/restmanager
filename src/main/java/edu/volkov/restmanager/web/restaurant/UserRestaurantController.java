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

import java.time.LocalDate;

import static edu.volkov.restmanager.util.RestaurantUtil.getTo;

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
        log.info("getAllWithPreassignedQuantityEnabledMenu for user {}", userId);

        model.addAttribute("restaurants", getTo(service.getAllWithPreassignedQuantityEnabledMenu()));
        return "restaurants";
    }

    @GetMapping("/filter")
    public String getFilteredByNameAndAddressWithEnabledMenu(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            Model model
    ) {
        int userId = SecurityUtil.authUserId();
        log.info("getFilteredByNameAndAddressWithEnabledMenu for user {}", userId);

        model.addAttribute("restaurants", getTo(service.getFilteredByNameAndAddressWithEnabledMenu(name, address)));
        return "restaurants";
    }

    @GetMapping("/vote")
    public String vote(Integer id) {
        service.vote(SecurityUtil.authUserId(), id);
        return "redirect:/restaurants";
    }
}
