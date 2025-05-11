package com.mizilin.firstbot.service;

import com.mizilin.firstbot.dto.QuizDto;
import com.mizilin.firstbot.entity.*;
import com.mizilin.firstbot.mappers.QuizMapper;
import com.mizilin.firstbot.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    private final QuizCacheService quizCacheService;
    private final Map<Long, QuizSession> sessions = new ConcurrentHashMap<>();


    @Autowired
    public QuizService(QuizRepository quizRepository, QuizMapper quizMapper, QuizCacheService quizCacheService) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
        this.quizCacheService = quizCacheService;
    }

    @Transactional
    public void startQuiz(Long chatId, Long quizId) {
        Quiz quiz = quizCacheService.getCachedQuiz(quizId);
        if (quiz == null) {
            quiz = quizRepository.findWithQuestionsAndOptionsAndResultsById(quizId);
            quizCacheService.cacheQuiz(quizId, quiz);

        }
        List<Question> questions = new ArrayList<>(quiz.getQuestions());
        List<Result> results = new ArrayList<>(quiz.getResults());
        sessions.put(chatId, new QuizSession(questions, results));
    }

    public Question getNextQuestion(Long chatId) {
        QuizSession session = sessions.get(chatId);
        return session.getNextQuestion();
    }

    public boolean hasMoreQuestions(Long chatId) {
        QuizSession session = sessions.get(chatId);
        return session.hasMoreQuestions();
    }

    public void processAnswer(Long chatId, String answer) {
        QuizSession session = sessions.get(chatId);
        session.processAnswer(answer);
    }

    public String getResult(Long chatId) {
        QuizSession session = sessions.get(chatId);
        String result = session.getResultText();
        sessions.remove(chatId);
        return result;
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Transactional
    public void saveQuiz(QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());
        quiz = quizRepository.save(quiz);
        quizRepository.save(quizMapper.toEntity(quiz, quizDto));
    }



    @Transactional
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
        quizCacheService.deleteCachedQuiz(id);
    }
}
