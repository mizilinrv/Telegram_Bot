package com.mizilin.firstbot.dto;

import lombok.Data;

@Data
public class OptionDto {
    private String text; // Текст ответа ("Часто", "Редко", ...)
    private int points; // Баллы за ответ
}