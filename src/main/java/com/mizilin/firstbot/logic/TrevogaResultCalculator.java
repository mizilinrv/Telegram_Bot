package com.mizilin.firstbot.logic;

import com.mizilin.firstbot.entity.Question;

import java.util.List;

public class TrevogaResultCalculator implements ResultCalculator {
    @Override
    public String calculate(List<Question> questions, List<String> answers) {
        int totalPoints = 0;
        for (int i = 0; i < questions.size(); i++) {
            String answer = answers.get(i);
            totalPoints += Integer.parseInt(answer);
        }
        if (totalPoints < 9 )
            return "Отсутствие тревоги.";
        else if (totalPoints < 21)
            return "Незначительный уровень тревоги";
        else  if (totalPoints  < 35)
            return "Средний уровень тревоги";
        else return "Очень высокий уровень тревоги";
    }
}
