package com.mizilin.firstbot.mappers;

import com.mizilin.firstbot.dto.QuestionDto;
import com.mizilin.firstbot.entity.Question;
import com.mizilin.firstbot.entity.Quiz;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public Question toEntity(QuestionDto questionDto, Quiz quiz) {
        Question question = new Question();
        question.setText(questionDto.getText());
        question.setQuiz(quiz);
        return question;
    }
}
