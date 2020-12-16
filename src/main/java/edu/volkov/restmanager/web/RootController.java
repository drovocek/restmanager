package edu.volkov.restmanager.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "forward:/rest/any/restaurants";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String getUsers() {
        return "forward:/rest/admin/users";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }
}
