package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.dto.QuizDto;
import com.mizilin.firstbot.entity.Quiz;
import com.mizilin.firstbot.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/list")
    public String listQuizzes(Model model) {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        model.addAttribute("quizzes", quizzes);
        return "quiz-list";
    }

    @PostMapping("/delete/{id}")
    public String deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return "redirect:/quiz/list?success";
    }

    @GetMapping("/create")
    public String showQuizForm() {
        return "addQuiz";
    }

    @PostMapping("/addQuiz")
    public String saveQuiz(@ModelAttribute QuizDto quizDto) {
        quizService.saveQuiz(quizDto);
        return "redirect:/quiz/create?success";

    }
}