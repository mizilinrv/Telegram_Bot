package com.mizilin.firstbot.service;

import com.mizilin.firstbot.dto.OptionDto;
import com.mizilin.firstbot.dto.QuizDto;
import com.mizilin.firstbot.entity.*;
import com.mizilin.firstbot.logic.*;
import com.mizilin.firstbot.mappers.OptionMapper;
import com.mizilin.firstbot.mappers.QuizMapper;
import com.mizilin.firstbot.repository.QuizRepository;
import com.mizilin.firstbot.repository.UniqueUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UniqueUserRepository uniqueUserRepository;
    private List<Question> currentQuestions;
    private List<String> userAnswers;
    private int currentQuestionIndex;
    private final CalculatorProvider calculatorProvider;
    private final OptionMapper optionMapper;
    private final QuizMapper quizMapper;

    @Autowired
    public QuizService(QuizRepository quizRepository, UniqueUserRepository uniqueUserRepository,
                       CalculatorProvider calculatorProvider, OptionMapper optionMapper, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.uniqueUserRepository = uniqueUserRepository;
        this.calculatorProvider = calculatorProvider;
        this.optionMapper = optionMapper;
        this.quizMapper = quizMapper;
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
        ResultCalculator calculator = new StandardResultCalculator();
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
    @Transactional
    public void saveQuiz(QuizDto quizDto) {

            Quiz quiz = new Quiz();
            quiz.setTitle(quizDto.getTitle());

            quiz = quizRepository.save(quiz);

            quizRepository.save(quizMapper.toEntity(quiz, quizDto));
        }

        }
