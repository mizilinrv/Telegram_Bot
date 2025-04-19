package com.mizilin.firstbot.utils;

import com.mizilin.firstbot.entity.Quiz;
import com.mizilin.firstbot.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TelegramUtils {

    @Value("${buttons.textButtonExternalQuiz}")
    private String textButtonExternalQuiz;
    @Value("${buttons.linkButtonExternalQuiz}")
    private String linkButtonExternalQuiz;
    private final QuizService quizService;
    @Autowired
    public TelegramUtils(QuizService quizService) {
        this.quizService = quizService;
    }

    public InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    public InlineKeyboardButton createUrlButton(String text, String url) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setUrl(url);
        return button;
    }

    public InlineKeyboardMarkup createMarkup(List<List<InlineKeyboardButton>> rows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
    public InlineKeyboardMarkup createQuizSelectionMarkup() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Quiz quiz : quizService.getAllQuizzes()) {
            List<InlineKeyboardButton> row = Collections.singletonList(
                    createButton(quiz.getTitle(), "quiz_" + quiz.getId()));
            rows.add(row);
        }
        List<InlineKeyboardButton> row2 = Collections.singletonList(
                createUrlButton(textButtonExternalQuiz, linkButtonExternalQuiz));
        rows.add(row2);
        return createMarkup(rows);
    }
}
