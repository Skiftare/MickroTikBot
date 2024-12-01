package edu.handles.commands.enteties;


import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.List;


public class AuthorsCommand implements Command {
    private final static boolean IS_VISIBLE_FOR_KEYBOARD = true;
    private final static String COMMAND_NAME = "/authors";
    private final static String COMMAND_DESCRIPTION = "Получить список авторов этого проекта";
    private final static List<String> AUTHORS = List.of("artem");

    @Override
    public SendMessage execute(UserMessageFromBotWrapper update) {

        String responseText = String.join("\n", AUTHORS);

        SendMessage message = new SendMessage();
        message.setChatId(update.userId());
        message.setText(responseText);
        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return IS_VISIBLE_FOR_KEYBOARD;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return true;
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
