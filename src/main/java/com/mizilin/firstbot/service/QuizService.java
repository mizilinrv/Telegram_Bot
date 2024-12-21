package com.mizilin.firstbot.service;

import com.mizilin.firstbot.entity.*;
import com.mizilin.firstbot.logic.*;
import com.mizilin.firstbot.repository.QuizRepository;
import com.mizilin.firstbot.repository.UniqueUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UniqueUserRepository uniqueUserRepository;
    private List<Question> currentQuestions;
    private List<String> userAnswers;
    private int currentQuestionIndex;
    private final CalculatorProvider calculatorProvider;

    @Autowired
    public QuizService(QuizRepository quizRepository, UniqueUserRepository uniqueUserRepository,
                       CalculatorProvider calculatorProvider) {
        this.quizRepository = quizRepository;
        this.uniqueUserRepository = uniqueUserRepository;
        this.calculatorProvider = calculatorProvider;
    }

    @Transactional
    public void startQuiz(Long quizId) {
        Quiz quiz = quizRepository.findWithQuestionsAndOptionsById(quizId);
        this.currentQuestions = new ArrayList<>(quiz.getQuestions());
        this.userAnswers = new ArrayList<>();
        this.currentQuestionIndex = 0;
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Question getNextQuestion() {
        return currentQuestions.get(currentQuestionIndex++);
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < currentQuestions.size();
    }

    public void processAnswer(String answer) {
        userAnswers.add(answer);
    }

    public String getResult() {
        Quiz quiz = quizRepository.findById(currentQuestions.get(0).getQuiz().getId()).orElseThrow();
        ResultCalculator calculator = calculatorProvider.getCalculator(quiz.getCalculationMethod());
        return calculator.calculate(currentQuestions, userAnswers);
    }

    @Transactional
    public void saveUser(long chatId) {
        uniqueUserRepository.save(new UniqueUser(chatId));
    }

    public String userCount() {
        return String.valueOf(uniqueUserRepository.count());
    }

    public List<UniqueUser> allUsers() {
        return uniqueUserRepository.findAll();
    }
}