package com.mizilin.firstbot.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizDto {
    private String title; // Название теста
    private List<OptionDto> options; // Общие ответы
    private List<QuestionDto> questions; // Список вопросов
    private List<ResultDto> results;
}
