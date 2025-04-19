package com.mizilin.firstbot.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizDto {
    private Long id;
    private String title;
    private List<OptionDto> options;
    private List<QuestionDto> questions;
    private List<ResultDto> results;
}
