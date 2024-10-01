package edu.handles.commands.enteties;


import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static edu.Configuration.SSHConnection.establishingSSH;

public class StateCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = false;
    private static final String COMMAND_DESCRIPTION = "Проверка работоспособности SSH соединения";
    private static final String COMMAND_NAME = "/state";

    @Override
    public SendMessage execute(Update update) {

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        String stateMessage = establishingSSH();
        message.setText(stateMessage + "\n"
                + "Эта команда доступна только "
                + "зарегестрированным пользователям. "
                + "Позволяет проверить, в каком состоянии находится наш роутер.");
        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return IS_VISIBLE_FOR_KEYBOARD;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {

        return (status == UserProfileStatus.NO_VPN || status == UserProfileStatus.ACTIVE_VPN);

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
