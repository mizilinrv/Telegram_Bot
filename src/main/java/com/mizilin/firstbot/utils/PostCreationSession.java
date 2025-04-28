package com.mizilin.firstbot.utils;

import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class PostCreationSession {
    private final long chatId;
    private String postText;
    private Integer buttonCount;
    private List<String> buttonTexts;

    public PostCreationSession(long chatId) {
        this.chatId = chatId;
    }

    public void initButtonTexts() {
        this.buttonTexts = new ArrayList<>();
    }

    public void addButtonText(String text) {
        buttonTexts.add(text);
    }

    public boolean isWaitingForButtons() {
        return buttonTexts != null && buttonTexts.size() < buttonCount;
    }

    public boolean isAllButtonsCollected() {
        return buttonTexts != null && buttonTexts.size() == buttonCount;
    }

    public int getCollectedButtonCount() {
        return buttonTexts == null ? 0 : buttonTexts.size();
    }
}
