package edu.handles.commands.enteties;


import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import edu.handles.commands.Command;


public class AuthorsCommand implements Command {
    private static final boolean IS_VISIBLE_FOR_KEYBOARD = true;
    private static final String COMMAND_NAME = "/authors";
    private static final String COMMAND_DESCRIPTION = "Получить список авторов этого проекта";
    private static final List<String> AUTHORS = Arrays.asList("skif", "artem");

    @Override
    public SendMessage execute(Update update) {

        String responseText = String.join("\n", AUTHORS);

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(responseText);
        return message;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return IS_VISIBLE_FOR_KEYBOARD;
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
