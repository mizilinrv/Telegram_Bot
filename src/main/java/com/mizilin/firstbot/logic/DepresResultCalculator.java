package com.mizilin.firstbot.logic;

import com.mizilin.firstbot.entity.Question;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class DepresResultCalculator implements ResultCalculator {
    @Override
    public String calculate(List<Question> questions, List<String> answers) {
        int totalPoints = 0;
        for (int i = 0; i < questions.size(); i++) {
            String answer = answers.get(i);
            totalPoints += Integer.parseInt(answer);
        }
        if (totalPoints <= 9 )
            return "Отсутствие депрессивных симптомов";
        else if (totalPoints <= 15)
            return "Легкая депрессия (субдепрессия)";
        else  if (totalPoints  <= 19)
            return "Умеренная депрессия";
        else  if (totalPoints  <=29)
            return "Выраженная депрессия (средней тяжести)";
        else return "Тяжелая депрессия";
    }
}
