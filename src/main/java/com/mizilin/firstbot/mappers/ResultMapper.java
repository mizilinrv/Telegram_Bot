package com.mizilin.firstbot.mappers;

import com.mizilin.firstbot.dto.ResultDto;
import com.mizilin.firstbot.entity.Quiz;
import com.mizilin.firstbot.entity.Result;
import org.springframework.stereotype.Component;

@Component
public class ResultMapper {

    public Result toEntity(ResultDto resultDto, Quiz quiz) {
        Result result = new Result();
        result.setText(resultDto.getText());
        result.setMinPoints(resultDto.getMinPoints());
        result.setMaxPoints(resultDto.getMaxPoints());
        result.setQuiz(quiz);
        return result;
    }
}
