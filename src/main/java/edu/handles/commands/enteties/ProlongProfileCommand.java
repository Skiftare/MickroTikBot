package edu.handles.commands.enteties;


import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static edu.Configuration.SecretProlongator.prolongSecret;

public class ProlongProfileCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = false;
    private static final String COMMAND_DESCRIPTION = "Команда для продления VPN сертификата";
    private static final String COMMAND_NAME = "/prolong";

    @Override
    public SendMessage execute(Update update) {

        SendMessage message = new SendMessage();
        Long tgUserId = update.getMessage().getFrom().getId();
        message.setChatId(tgUserId);

        String stateMessage = prolongSecret(tgUserId);

        message.setText(stateMessage + "\n\n"
                + "Эта команда доступна только "
                + "зарегестрированным пользователям. "
                + "Позволяет продлить VPN подключение.");
        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return IS_VISIBLE_FOR_KEYBOARD;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {

        return (status == UserProfileStatus.ACTIVE_VPN);

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
