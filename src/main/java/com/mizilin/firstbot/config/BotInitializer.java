package com.mizilin.firstbot.config;

import com.mizilin.firstbot.QuizBot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
@RequiredArgsConstructor
public final class BotInitializer {

    private final QuizBot quizBot;

    @PostConstruct
    public void init() {
        try {
            final TelegramBotsApi botsApi =
                    new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(quizBot);
        } catch (final TelegramApiException e) {
            log.error(String.valueOf(e));
        }
    }
}
