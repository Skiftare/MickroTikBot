package edu.handles.commands.enteties;


import edu.Configuration.SSHConnection;
import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public class ProfileCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = false;
    private static final String COMMAND_DESCRIPTION = "Просмотреть состояние роутера";
    private static final String COMMAND_NAME = "/chr_profile";

    @Override
    public SendMessage execute(UserMessageFromBotWrapper update) {
        SendMessage message = new SendMessage();
        Long tgUserId = update.userId();
        message.setChatId(tgUserId);

        message.setText(SSHConnection.establishingSSH());
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
