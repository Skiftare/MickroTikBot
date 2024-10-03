package edu.handles.commands.enteties;


import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static edu.Configuration.SSHConnection.establishingSSH;
import edu.handles.commands.Command;

public class StateCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = false;
    private static final String COMMAND_DESCRIPTION = "Проверка работоспособности SSH соединения";
    private static final String COMMAND_NAME = "/state";

    @Override
    public SendMessage execute(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        String stateMessage = establishingSSH();
        message.setText(stateMessage);
        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return IS_VISIBLE_FOR_KEYBOARD;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        if(status == UserProfileStatus.GUEST) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
