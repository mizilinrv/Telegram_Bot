package com.mizilin.firstbot.mappers;

import com.mizilin.firstbot.dto.OptionDto;
import com.mizilin.firstbot.entity.Option;
import com.mizilin.firstbot.entity.Quiz;
import org.springframework.stereotype.Component;

@Component
public class OptionMapper {
    public Option toEntity(OptionDto optionDto, Quiz quiz) {
        Option option = new Option();
        option.setText(optionDto.getText());
        option.setPoints(optionDto.getPoints());
        option.setQuiz(quiz);
        return option;
    }

}
