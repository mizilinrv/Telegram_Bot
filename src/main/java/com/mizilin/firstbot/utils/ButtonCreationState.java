package com.mizilin.firstbot.utils;

import java.util.ArrayList;
import java.util.List;

public class ButtonCreationState {
    public int buttonCount;
    public List<String> buttonTexts = new ArrayList<>();
    public Stage stage;
    public String postText;

    public enum Stage {
        AWAITING_BUTTON_COUNT,
        AWAITING_BUTTON_TEXTS,
        CONFIRMATION,
        AWAITING_POST_TEXT
    }
}
