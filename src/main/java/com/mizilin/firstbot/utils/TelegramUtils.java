package com.mizilin.firstbot.utils;

import com.mizilin.firstbot.entity.Quiz;
import com.mizilin.firstbot.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramUtils {

    @Value("${textButtonExternalQuiz}")
    private String textButtonExternalQuiz;
    @Value("${linkButtonExternalQuiz}")
    private String linkButtonExternalQuiz;
    private final QuizService quizService;
    public InlineKeyboardButton createButton(final String text, final String callbackData) {
        final InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    public InlineKeyboardButton createUrlButton(final String text, final String url) {
        final InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setUrl(url);
        return button;
    }

    public InlineKeyboardMarkup createMarkup(final List<List<InlineKeyboardButton>> rows) {
        final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
    public InlineKeyboardMarkup createQuizSelectionMarkup() {
        final List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (final Quiz quiz : quizService.getAllQuizzes()) {
            final List<InlineKeyboardButton> row = Collections.singletonList(
                    createButton(quiz.getTitle(), "quiz_" + quiz.getId()));
            rows.add(row);
        }
        final List<InlineKeyboardButton> row2 = Collections.singletonList(
                createUrlButton(textButtonExternalQuiz, linkButtonExternalQuiz));
        rows.add(row2);
        return createMarkup(rows);
    }
}
