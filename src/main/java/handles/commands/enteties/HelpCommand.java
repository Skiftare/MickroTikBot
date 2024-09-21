package handles.commands.enteties;

import handles.commands.Command;
import handles.tables.CommandTable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

public class HelpCommand implements Command {

    private static final boolean IS_VISIBLE_FOR_KEYBOARD = true;
    private static final String COMMAND_NAME = "/help";
    private static final String COMMAND_DESCRIPTION = "Получить список всех доступных команд";


    private String responseText;

    public HelpCommand(CommandTable table) {

        responseText = table.getCommands().values().stream()
                .map(command -> command.getCommandName() + " - " + command.getCommandDescription())
                .collect(Collectors.joining("\n"));
        responseText += "\n" + COMMAND_NAME + " - " + COMMAND_DESCRIPTION;


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
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
