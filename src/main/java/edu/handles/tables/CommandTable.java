package edu.handles.tables;

import edu.handles.commands.Command;
import java.util.HashMap;

public class CommandTable {
    private HashMap<String, Command> commandHashMap = new HashMap<>();

    public CommandTable(Command... commands) {
        for (Command command : commands) {
            commandHashMap.put(command.getCommandName(), command);
        }
    }

    public CommandTable(CommandTable preCommandTable, Command... additionalCommands) {
        // Добавляем команды из другой таблицы
        this.commandHashMap.putAll(preCommandTable.getCommands());
        // Добавляем дополнительные команды
        for (Command command : additionalCommands) {
            commandHashMap.put(command.getCommandName(), command);
        }
    }

    public void addCommandsFrom(CommandTable otherTable) {
        commandHashMap.putAll(otherTable.getCommands());
    }

    public HashMap<String, Command> getCommands() {
        return new HashMap<>(commandHashMap);
    }
}
