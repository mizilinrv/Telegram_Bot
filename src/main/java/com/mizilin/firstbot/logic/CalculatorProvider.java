package com.mizilin.firstbot.logic;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class CalculatorProvider {
    private final Map<String, ResultCalculator> calculators = new HashMap<>();

    public CalculatorProvider() {
        calculators.put("standard", new StandardResultCalculator());
        calculators.put("good", new GoodResultCalculator());
        calculators.put("social", new SocialCalculator());
        calculators.put("depres", new DepresResultCalculator());
        calculators.put("trevoga", new TrevogaResultCalculator());
        // Добавьте другие реализации калькуляторов здесь
    }

    public ResultCalculator getCalculator(String type) {
        return calculators.get(type);
    }
}

