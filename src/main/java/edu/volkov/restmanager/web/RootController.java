package edu.volkov.restmanager.web;

import edu.volkov.restmanager.model.Restaurant;
import edu.volkov.restmanager.service.UserService;
import edu.volkov.restmanager.to.RestaurantTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.function.Predicate;

import static edu.volkov.restmanager.util.model.RestaurantUtil.getFilteredTosWithMenu;

@Controller
public class RootController {

    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @PostMapping("/users")
    public String setUser(@RequestParam(required = false) Integer userId) {
        SecurityUtil.setAuthUserId(userId);
        return "redirect:restaurants";
    }
}
