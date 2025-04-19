package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.QuizBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BroadcastController {

    private final QuizBot quizBot;

    @Autowired
    public BroadcastController(QuizBot quizBot) {
        this.quizBot = quizBot;
    }

    @GetMapping("/post")
    public String showNewPostForm() {
        return "post";
    }

    @PostMapping("/sendBroadcastMessage")
    public String sendBroadcastMessage(@RequestParam("message") String message) {
        quizBot.sendBroadcastMessage(message);
        return "redirect:/post?success";
    }
}
