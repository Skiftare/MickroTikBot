package edu.handles.commands;

import edu.models.UserProfileStatus;

public interface Command {

    BotResponseToUserWrapper execute(UserMessageFromBotWrapper userMessage);

    boolean isVisibleForKeyboard();

    boolean isVisibleForKeyboard(UserProfileStatus status);

    String getCommandName();

    String getCommandDescription();
}
