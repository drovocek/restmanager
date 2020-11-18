package edu.volkov.restmanager.web;

import edu.volkov.restmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @PostMapping("/users")
    public String setUser(@RequestParam(name = "userId", required = false) Integer userId) {
        SecurityUtil.setAuthUserId(userId);
        if (SecurityUtil.isAdmin()) {
            return "redirect:restaurantManaging";
        }
        return "redirect:restaurantMenuVote";
    }
}
