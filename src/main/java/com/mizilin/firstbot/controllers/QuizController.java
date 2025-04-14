package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.dto.QuizDto;
import com.mizilin.firstbot.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }


    @GetMapping("/quiz/create")
    public String showQuizForm() {
        return "addQuiz"; // HTML-файл в папке templates
    }

    @PostMapping("/addQuiz")
    public String saveQuiz(@ModelAttribute QuizDto quizDto, RedirectAttributes redirectAttributes) {
        quizService.saveQuiz(quizDto);
        redirectAttributes.addFlashAttribute("success", "Тест успешно сохранен!");
        return "redirect:/quiz/create";
    }
}