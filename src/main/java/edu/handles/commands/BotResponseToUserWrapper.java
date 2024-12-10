package edu.handles.commands;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public record BotResponseToUserWrapper(
        Long userId,
        String message,
        boolean isMarkdownEnabled,
        ReplyKeyboardMarkup keyboardMarkup,
        String imageLink,
        String videoLink
) {
    public BotResponseToUserWrapper(
            Long userId,
            String message) {
        this(userId, message, false, null, null, null);
    }

    //конструктор со ссылкой на фотографию или видео
    public BotResponseToUserWrapper(
            Long userId,
            String message, String imageLink, String videoLink) {
        this(userId, message, false, null, imageLink, videoLink);
    }

}
