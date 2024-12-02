package edu.handles.commands.enteties;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import edu.handles.commands.BotResponseToUserWrapper;

import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.handles.tables.CommandTable;
import edu.models.UserProfileStatus;

public class HelpCommand implements Command {

    private final static boolean IS_VISIBLE_FOR_KEYBOARD = true;
    private final static String COMMAND_NAME = "/help";
    private final static String COMMAND_DESCRIPTION = "Получить список всех доступных команд";

    private final Map<UserProfileStatus, String> statusResponses;

    public HelpCommand(CommandTable table) {
        String separatorInLine = " - ";
        statusResponses = new EnumMap<>(UserProfileStatus.class);
        // Подготавливаем ответы для каждого статуса
        for (UserProfileStatus status : UserProfileStatus.values()) {
            String response = table.getCommands().values().stream()
                    .filter(command -> command.isVisibleForKeyboard(status))
                    .map(command -> command.getCommandName() + separatorInLine + command.getCommandDescription())
                    .collect(Collectors.joining("\n"));
            statusResponses.put(status, response);
        }
    }

    @Override
    public BotResponseToUserWrapper execute(UserMessageFromBotWrapper wrapper) {
        return new BotResponseToUserWrapper(wrapper.userId(), statusResponses.get(wrapper.status()));
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
