package com.mizilin.firstbot.service;

import com.mizilin.firstbot.dto.QuizDto;
import com.mizilin.firstbot.entity.Question;
import com.mizilin.firstbot.entity.Quiz;
import com.mizilin.firstbot.entity.Result;
import com.mizilin.firstbot.mappers.QuizMapper;
import com.mizilin.firstbot.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    private final Map<Long, QuizSession> sessions = new ConcurrentHashMap<>();

    @Transactional
    public void startQuiz(final Long chatId, final Long quizId) {
        Quiz quiz = quizRepository.findWithQuestionsAndOptionsAndResultsById(quizId);
        final List<Question> questions = new ArrayList<>(quiz.getQuestions());
        final List<Result> results = new ArrayList<>(quiz.getResults());
        sessions.put(chatId, new QuizSession(questions, results));
    }

    public Question getNextQuestion(final Long chatId) {
        final QuizSession session = sessions.get(chatId);
        return session.getNextQuestion();
    }

    public boolean hasMoreQuestions(final Long chatId) {
        final QuizSession session = sessions.get(chatId);
        return session.hasMoreQuestions();
    }

    public void processAnswer(final Long chatId, final String answer) {
        final QuizSession session = sessions.get(chatId);
        session.processAnswer(answer);
    }

    public String getResult(final Long chatId) {
        final QuizSession session = sessions.get(chatId);
        final String result = session.getResultText();
        sessions.remove(chatId);
        return result;
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Transactional
    public void saveQuiz(final QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());
        quiz = quizRepository.save(quiz);
        quizRepository.save(quizMapper.toEntity(quiz, quizDto));
    }

    @Transactional
    public void deleteQuiz(final Long id) {
        quizRepository.deleteById(id);
    }
}
