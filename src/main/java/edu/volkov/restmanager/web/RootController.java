package edu.volkov.restmanager.web;

import edu.volkov.restmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

    @Autowired
    private UserService service;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", service.getAll());
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
