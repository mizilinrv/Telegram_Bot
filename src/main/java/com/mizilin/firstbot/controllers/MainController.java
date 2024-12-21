package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class MainController {

    private final QuizService quizService;
    @Autowired
    public MainController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/")
    public String home(Model model) {
        String userCount = quizService.userCount();
        model.addAttribute("userCount", userCount);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
