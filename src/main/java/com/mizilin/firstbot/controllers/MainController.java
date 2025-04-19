package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class MainController {
    private final UserService userService;
    @Autowired
    public MainController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        String userCount = userService.userCount();
        model.addAttribute("userCount", userCount);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
