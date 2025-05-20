package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.QuizBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BroadcastController {

    private final QuizBot quizBot;
    @GetMapping("/post")
    public String showNewPostForm() {
        return "post";
    }

    @PostMapping("/sendBroadcastMessage")
    public String sendBroadcastMessage(@RequestParam("message") final String message) {
        quizBot.sendBroadcastMessage(message);
        return "redirect:/post?success";
    }
}
