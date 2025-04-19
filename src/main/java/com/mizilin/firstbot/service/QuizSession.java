package com.mizilin.firstbot.service;

import com.mizilin.firstbot.entity.Question;
import com.mizilin.firstbot.entity.Result;

import java.util.List;

public class QuizSession {

    private final List<Question> questions;
    private final List<Result> results;
    private int currentQuestionIndex = 0;
    private int userPoints = 0;


    public QuizSession(List<Question> questions, List<Result> results) {
        this.questions = questions;
        this.results = results;
    }

    public Question getNextQuestion() {
        return questions.get(currentQuestionIndex++);
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size();
    }

    public void processAnswer(String answer) {
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
