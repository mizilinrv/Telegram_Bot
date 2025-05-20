package com.mizilin.firstbot;

import com.mizilin.firstbot.config.BotConfig;
import com.mizilin.firstbot.entity.Option;
import com.mizilin.firstbot.entity.Question;
import com.mizilin.firstbot.entity.UniqueUser;
import com.mizilin.firstbot.service.QuizService;
import com.mizilin.firstbot.service.UserService;
import com.mizilin.firstbot.utils.TelegramUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
@Component
@PropertySource(value = "classpath:messageBot.properties", encoding = "UTF-8")
public class QuizBot extends TelegramLongPollingBot {
    private final QuizService quizService;
    private final BotConfig botConfig;
    private final TelegramUtils telegramUtils;
    private final UserService userService;

    @Value("${helloMessage}")
    private String helloMessage;
    @Value("${textButtonAccount}")
    private String textButtonAccount;
    @Value("${linkButtonAccount}")
    private String linkButtonAccount;
    @Value("${unknownCommandMessage}")
    private String unknownCommandMessage;
    @Value("${helloFirstMessage}")
    private String helloFirstMessage;


    @Autowired
    public QuizBot(final QuizService quizService, final BotConfig botConfig, final TelegramUtils telegramUtils, final UserService userService) {
        super(botConfig.getBotToken());
        this.quizService = quizService;
        this.botConfig = botConfig;
        this.telegramUtils = telegramUtils;
        this.userService = userService;

    }
    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    //Проверка, есть ли новые сообщения
    @Override
    public void onUpdateReceived(final Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleIncomingMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    //Обработка команд из бота
    private void handleIncomingMessage(final Message message) {
        final String messageText = message.getText();
        final long chatId = message.getChatId();
        final long telegramId = message.getFrom().getId();
        userService.saveUser(telegramId);
            switch (messageText) {
                case "/start":
                    sendPhoto(chatId);
                    sendQuizSelection(chatId);
                    break;
                case "/psychologist":
                    sendPsychologistLink(chatId);
                    break;
                default:
                    sendUnknownCommandMessage(chatId);
                    break;
            }
        }


    // Обработка возвращенных данных с кнопок
    private void handleCallbackQuery(final CallbackQuery callbackQuery) {
        final String callbackData = callbackQuery.getData();
        final long chatId = callbackQuery.getMessage().getChatId();
        final int messageId = callbackQuery.getMessage().getMessageId();

        if (callbackData.equals("start_over")) {
            sendPhoto(chatId);
            sendQuizSelection(chatId);
        } else if (callbackData.startsWith("quiz_")) {
            final Long quizId = Long.parseLong(callbackData.split("_")[1]);
            quizService.startQuiz(chatId, quizId);
            sendQuestion(chatId, messageId, quizService.getNextQuestion(chatId));
        } else {
            quizService.processAnswer(chatId, callbackData);
            if (quizService.hasMoreQuestions(chatId)) {
                sendQuestion(chatId, messageId, quizService.getNextQuestion(chatId));
            } else {
                sendResult(chatId, messageId, quizService.getResult(chatId));
            }
        }
    }

    //Отправка выбора теста
    public void sendQuizSelection(final long chatId) {
        final SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(helloMessage);
        message.setReplyMarkup(telegramUtils.createQuizSelectionMarkup());
        try {
            execute(message);
        } catch (final TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    //Отправка вопросов теста
    public void sendQuestion(final long chatId, final int messageId, final Question question) {
        final List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (final Option option : question.getQuiz().getOptions()) {
            final List<InlineKeyboardButton> row = Collections.singletonList(
                    telegramUtils.createButton(option.getText(), String.valueOf(option.getPoints())));
            rows.add(row);
        }
        final InlineKeyboardMarkup markup = telegramUtils.createMarkup(rows);
        editMessage(chatId, messageId, question.getText(), markup);
    }

    //Отправка результата теста
    public void sendResult(final long chatId, final int messageId, final String result) {
        final List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        final List<InlineKeyboardButton> startButton = Collections.singletonList(
                telegramUtils.createButton("Выбрать новый тест", "start_over"));
        final List<InlineKeyboardButton> accountButton = Collections.singletonList(
                telegramUtils.createUrlButton(textButtonAccount, linkButtonAccount));
        rows.add(startButton);
        rows.add(accountButton);
        final InlineKeyboardMarkup markup = telegramUtils.createMarkup(rows);
        editMessage(chatId, messageId, "Результат теста: " + result, markup);
    }

    //Отправка ссылки
    public void sendPsychologistLink(final long chatId) {
        sendMessage(chatId, linkButtonAccount);
    }

    //Отправка - команда неизвестна
    public void sendUnknownCommandMessage(final long chatId) {
        sendMessage(chatId, unknownCommandMessage);
    }

    //Отправить сообщение всем
    public void sendBroadcastMessage(final String messageText) {
        for (final UniqueUser uniqueUser : userService.allUsers()) {
            sendMessage(uniqueUser.getId(), messageText);
        }
    }

    //Отправка фото
    public void sendPhoto(final long chatId) {
        final SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(chatId));
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("photo.PNG");
        if (inputStream != null) {
            sendPhotoRequest.setPhoto(new InputFile(inputStream, "photo.PNG"));
        }
        sendPhotoRequest.setCaption(helloFirstMessage);
        sendPhotoRequest.setParseMode("Markdown");
        try {
            execute(sendPhotoRequest);
        } catch (final TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    //Отправка нового сообщения
    public void sendMessage(final long chatId, final String text) {
        final SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (final TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    //Отправка измененного сообщения
    public void editMessage(final long chatId, final int messageId, final String text, final InlineKeyboardMarkup markup) {
        final EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId(messageId);
        message.setText(text);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (final TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}



