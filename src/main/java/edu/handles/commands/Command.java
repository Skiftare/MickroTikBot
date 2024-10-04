package edu.handles.commands;

import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    SendMessage execute(Update update);

    boolean isVisibleForKeyboard();

    boolean isVisibleForKeyboard(UserProfileStatus status);

    String getCommandName();

    String getCommandDescription();
}
