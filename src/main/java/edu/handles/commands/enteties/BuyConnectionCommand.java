package edu.handles.commands.enteties;

import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BuyConnectionCommand implements Command {
    @Override
    public SendMessage execute(Update update) {
        return null;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return false;
    }

    @Override
    public String getCommandName() {
        return "";
    }

    @Override
    public String getCommandDescription() {
        return "";
    }
}
