package edu.handles.commands.enteties;

import edu.handles.commands.Command;
import edu.handles.tables.CommandTable;
import java.util.stream.Collectors;

import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;



public class HelpCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = true;
    private static final String COMMAND_NAME = "/help";
    private static final String COMMAND_DESCRIPTION = "Получить список всех доступных команд";



    private String responseText;

    public HelpCommand(CommandTable table) {
        String separatorInLine = " - ";
        responseText = table.getCommands().values().stream()
                .map(command -> command.getCommandName() + separatorInLine + command.getCommandDescription())
                .collect(Collectors.joining("\n"));
        responseText += "\n" + COMMAND_NAME + separatorInLine + COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage execute(Update update) {
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
