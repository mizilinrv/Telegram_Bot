package com.mizilin.firstbot.logic;

import com.mizilin.firstbot.entity.Question;

import java.util.List;

public class StandardResultCalculator implements ResultCalculator {
    @Override
    public String calculate(List<Question> questions, List<String> answers) {
        int totalPoints = 0;
        for (int i = 0; i < questions.size(); i++) {
            String answer = answers.get(i);
            totalPoints += Integer.parseInt(answer);
                }
        if (totalPoints < 30 )
        return "Очень мало созависимых и/или высокая степень контрзависимых моделей";
        else if (totalPoints < 40)
            return "Средняя степень созависимых и/или контрзависимых моделей";
        else  if (totalPoints <60)
            return "Высокая степень созависимых моделей";
        else return "Очень высокая степень созависимых моделей.";
    }
}