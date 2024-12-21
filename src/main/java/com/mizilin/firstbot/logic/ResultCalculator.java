package com.mizilin.firstbot.logic;

import com.mizilin.firstbot.entity.Question;

import java.util.List;

public interface ResultCalculator {
    String calculate(List<Question> questions, List<String> answers);
}