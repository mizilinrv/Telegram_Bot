package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    @GetMapping("/")
    public String home(final Model model) {
        final String userCount = userService.userCount();
        model.addAttribute("userCount", userCount);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
