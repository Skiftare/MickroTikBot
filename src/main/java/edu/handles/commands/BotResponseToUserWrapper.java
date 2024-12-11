package edu.handles.commands;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public record BotResponseToUserWrapper(
        Long userId,
        String message,
        boolean isMarkdownEnabled,
        ReplyKeyboardMarkup keyboardMarkup
) {
    public BotResponseToUserWrapper(
            Long userId,
            String message) {
        this(userId, message, false, null);
    }

}
