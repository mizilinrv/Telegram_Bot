package com.mizilin.firstbot.logic;

import com.mizilin.firstbot.entity.Question;

import java.util.List;

public class SocialCalculator implements ResultCalculator {
    @Override
    public String calculate(List<Question> questions, List<String> answers) {
        int totalPoints = 0;
        for (int i = 0; i < questions.size(); i++) {
            String answer = answers.get(i);
            totalPoints += Integer.parseInt(answer);
        }
        if (totalPoints < 55 )
            return "У вас нет социальной фобии, либо легко выраженная социальная фобия" +
                    "в некоторых сферах жизни";
        else if (totalPoints > 55 && totalPoints < 65)
            return "Лёгкая социальная фобия";
        else if (totalPoints > 65 && totalPoints < 80)
            return "Выраженная социальная фобия";
        else if (totalPoints > 80 && totalPoints < 95)
            return "Тяжелая социальная фобия";
        else return "Очень тяжелая социальная фобия" ;
    }
}
