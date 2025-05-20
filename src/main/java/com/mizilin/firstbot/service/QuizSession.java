package com.mizilin.firstbot.service;

import com.mizilin.firstbot.entity.Question;
import com.mizilin.firstbot.entity.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QuizSession {

    private final List<Question> questions;
    private final List<Result> results;
    private int currentQuestionIndex = 0;
    private int userPoints = 0;
    public Question getNextQuestion() {
        return questions.get(currentQuestionIndex++);
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size();
    }

    public void processAnswer(final String answer) {
        userPoints += Integer.parseInt(answer);
    }

    public String getResultText() {
        return results.stream()
                .filter(result -> userPoints >= result.getMinPoints() && userPoints <= result.getMaxPoints())
                .map(Result::getText)
                .findFirst()
                .orElse("Результат не найден");
    }
}
