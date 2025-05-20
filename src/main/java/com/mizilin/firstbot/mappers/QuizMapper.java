package com.mizilin.firstbot.mappers;

import com.mizilin.firstbot.dto.QuizDto;
import com.mizilin.firstbot.entity.Option;
import com.mizilin.firstbot.entity.Question;
import com.mizilin.firstbot.entity.Quiz;
import com.mizilin.firstbot.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuizMapper {
    private final OptionMapper optionMapper;
    private final QuestionMapper questionMapper;
    private final ResultMapper resultMapper;

    public Quiz toEntity(final Quiz quiz, final QuizDto dto) {

        Set<Question> questions = dto.getQuestions().stream()
                .map(questionDto -> questionMapper.toEntity(questionDto, quiz))
                .collect(Collectors.toSet());

        Set<Result> results = dto.getResults().stream()
                .map(resultDto -> resultMapper.toEntity(resultDto, quiz))
                .collect(Collectors.toSet());

        Set<Option> options = dto.getOptions().stream()
                .map(optionDto -> optionMapper.toEntity(optionDto, quiz))
                .collect(Collectors.toSet());

        quiz.setQuestions(questions);
        quiz.setResults(results);
        quiz.setOptions(options);

        return quiz;
    }
}
