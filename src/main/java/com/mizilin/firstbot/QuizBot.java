package com.mizilin.firstbot;

import com.mizilin.firstbot.config.BotConfig;
import com.mizilin.firstbot.entity.Option;
import com.mizilin.firstbot.entity.Question;
import com.mizilin.firstbot.entity.UniqueUser;
import com.mizilin.firstbot.service.QuizService;
import com.mizilin.firstbot.service.UserService;
import com.mizilin.firstbot.utils.ButtonCreationState;
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
import java.util.*;

@Slf4j
@Component
@PropertySource(value = "classpath:messageBot.properties", encoding = "UTF-8")
public class QuizBot extends TelegramLongPollingBot {
    private final QuizService quizService;
    private final BotConfig botConfig;
    private final TelegramUtils telegramUtils;
    private final UserService userService;
    private final Map<Long, ButtonCreationState> buttonCreationStates = new HashMap<>();

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
    public QuizBot(QuizService quizService, BotConfig botConfig, TelegramUtils telegramUtils, UserService userService) {
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
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleIncomingMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    //Обработка команд из бота
    private void handleIncomingMessage(Message message) {
        String messageText = message.getText();
        long chatId = message.getChatId();
        long telegramId = message.getFrom().getId();
        userService.saveUser(telegramId);
        //System.out.println(message.getForwardFromChat().getId());
        if (buttonCreationStates.containsKey(telegramId)) {
            if (messageText.equals("/start")) {
                resetButtonCreationProcess(telegramId, chatId);
                sendPhoto(chatId);
                sendQuizSelection(chatId);
            } else {
                processButtonCreationStep(telegramId, chatId, messageText);
            }
            return; // Прерываем обработку других команд
        }

            switch (messageText) {
                case "/start":
                    sendPhoto(chatId);
                    sendQuizSelection(chatId);
                    break;
                case "/psychologist":
                    sendPsychologistLink(chatId);
                    break;
                case "/createbutton":
                    startButtonCreationProcess(telegramId, chatId);
                    break;
                default:
                    sendUnknownCommandMessage(chatId);
                    break;
            }
        }


    // Обработка возвращенных данных с кнопок
    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        if (callbackData.equals("start_over")) {
            sendPhoto(chatId);
            sendQuizSelection(chatId);
        } else if (callbackData.startsWith("quiz_")) {
            Long quizId = Long.parseLong(callbackData.split("_")[1]);
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
    public void sendQuizSelection(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(helloMessage);
        message.setReplyMarkup(telegramUtils.createQuizSelectionMarkup());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    //Отправка вопросов теста
    public void sendQuestion(long chatId, int messageId, Question question) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Option option : question.getQuiz().getOptions()) {
            List<InlineKeyboardButton> row = Collections.singletonList(
                    telegramUtils.createButton(option.getText(), String.valueOf(option.getPoints())));
            rows.add(row);
        }
        InlineKeyboardMarkup markup = telegramUtils.createMarkup(rows);
        editMessage(chatId, messageId, question.getText(), markup);
    }

    //Отправка результата теста
    public void sendResult(long chatId, int messageId, String result) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> startButton = Collections.singletonList(
                telegramUtils.createButton("Выбрать новый тест", "start_over"));
        List<InlineKeyboardButton> accountButton = Collections.singletonList(
                telegramUtils.createUrlButton(textButtonAccount, linkButtonAccount));
        rows.add(startButton);
        rows.add(accountButton);
        InlineKeyboardMarkup markup = telegramUtils.createMarkup(rows);
        editMessage(chatId, messageId, "Результат теста: " + result, markup);
    }

    //Отправка ссылки
    public void sendPsychologistLink(long chatId) {
        sendMessage(chatId, linkButtonAccount);
    }

    //Отправка - команда неизвестна
    public void sendUnknownCommandMessage(long chatId) {
        sendMessage(chatId, unknownCommandMessage);
    }

    //Отправить сообщение всем
    public void sendBroadcastMessage(String messageText) {
        for (UniqueUser uniqueUser : userService.allUsers()) {
            sendMessage(uniqueUser.getId(), messageText);
        }
    }

    //Отправка фото
    public void sendPhoto(long chatId) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(chatId));
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("photo.PNG");
        if (inputStream != null) {
            sendPhotoRequest.setPhoto(new InputFile(inputStream, "photo.PNG"));
        }
        sendPhotoRequest.setCaption(helloFirstMessage);
        sendPhotoRequest.setParseMode("Markdown");
        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    //Отправка нового сообщения
    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    //Отправка измененного сообщения
    public void editMessage(long chatId, int messageId, String text, InlineKeyboardMarkup markup) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId(messageId);
        message.setText(text);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    private void startButtonCreationProcess(long telegramId, long chatId) {
        if (buttonCreationStates.containsKey(telegramId)) {
            sendMessage(chatId, "Вы уже начали процесс создания кнопок. Завершите его или введите /start для сброса.");
            return;
        }
        ButtonCreationState state = new ButtonCreationState();
        state.stage = ButtonCreationState.Stage.AWAITING_POST_TEXT;
        buttonCreationStates.put(telegramId, state);
        sendMessage(chatId, "Пришлите текст поста, к которому хотите добавить кнопки:");
    }

    private void processButtonCreationStep(long telegramId, long chatId, String messageText) {
        ButtonCreationState state = buttonCreationStates.get(telegramId);

        switch (state.stage) {
            case AWAITING_POST_TEXT:
                state.postText = messageText;
                state.stage = ButtonCreationState.Stage.AWAITING_BUTTON_COUNT;
                sendMessage(chatId, "Введите количество кнопок, которое хотите создать:");
                break;

            case AWAITING_BUTTON_COUNT:
                try {
                    int count = Integer.parseInt(messageText);
                    if (count <= 0 || count > 10) {
                        sendMessage(chatId, "Введите число от 1 до 10.");
                        return;
                    }
                    state.buttonCount = count;
                    state.buttonTexts.clear();
                    state.stage = ButtonCreationState.Stage.AWAITING_BUTTON_TEXTS;
                    sendMessage(chatId, "Введите текст для кнопки 1:");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Пожалуйста, введите корректное число.");
                }
                break;

            case AWAITING_BUTTON_TEXTS:
                state.buttonTexts.add(messageText);
                if (state.buttonTexts.size() < state.buttonCount) {
                    sendMessage(chatId, "Введите текст для кнопки " + (state.buttonTexts.size() + 1) + ":");
                } else {
                    sendPostPreviewWithButtons(chatId, state);
                    sendMessage(chatId, "Правильно ли создан пост с кнопками? (Да / Нет)");
                    state.stage = ButtonCreationState.Stage.CONFIRMATION;
                }
                break;

            case CONFIRMATION:
                if (messageText.equalsIgnoreCase("Да")) {
                    sendPostWithButtons(chatId, state);
                    sendCustomButtonsToChannel("-1002657134751",state);
                    buttonCreationStates.remove(telegramId);
                } else if (messageText.equalsIgnoreCase("Нет")) {
                    sendMessage(chatId, "Начнём заново.");
                    startButtonCreationProcess(telegramId, chatId);
                } else {
                    sendMessage(chatId, "Пожалуйста, ответьте 'Да' или 'Нет'.");
                }
                break;
        }
    }

    private void sendCustomButtons(long chatId, List<String> buttonTexts) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String text : buttonTexts) {
            List<InlineKeyboardButton> row = Collections.singletonList(
                    telegramUtils.createButton(text, text)); // callbackData = текст кнопки
            rows.add(row);
        }
        InlineKeyboardMarkup markup = telegramUtils.createMarkup(rows);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Вот ваши кнопки:");
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendCustomButtonsToChannel(String channelChatId, ButtonCreationState state) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String text : state.buttonTexts) {
            List<InlineKeyboardButton> row = Collections.singletonList(
                    telegramUtils.createButton(text, text));
            rows.add(row);
        }
        InlineKeyboardMarkup markup = telegramUtils.createMarkup(rows);

        SendMessage message = new SendMessage();
        message.setChatId(channelChatId);
        message.setText(state.postText);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }
    private void sendPostPreviewWithButtons(long chatId, ButtonCreationState state) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(state.postText);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (String btnText : state.buttonTexts) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(btnText);
            button.setCallbackData("callback_" + btnText); // пример callback data

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    // Отправляем итоговый пост с кнопками
    private void sendPostWithButtons(long chatId, ButtonCreationState state) {
        sendPostPreviewWithButtons(chatId, state);
        sendMessage(chatId, "Пост с кнопками успешно создан!");
    }

    // Сброс процесса создания кнопок
    private void resetButtonCreationProcess(long telegramId, long chatId) {
        buttonCreationStates.remove(telegramId);
        sendMessage(chatId, "Процесс создания кнопок отменён. Возвращаемся в главное меню.");
    }


}


