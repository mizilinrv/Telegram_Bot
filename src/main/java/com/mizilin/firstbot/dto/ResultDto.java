package com.mizilin.firstbot.dto;

import lombok.Data;

@Data
public class ResultDto {
    private int minPoints; // Минимальный балл
    private int maxPoints; // Максимальный балл
    private String text; // Текст результата (описание)
}

