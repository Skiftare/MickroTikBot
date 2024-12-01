package edu.handles.commands;

import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Command {

    SendMessage execute(UserMessageFromBotWrapper userMessage);

    boolean isVisibleForKeyboard();

    boolean isVisibleForKeyboard(UserProfileStatus status);

    String getCommandName();

    String getCommandDescription();
}
