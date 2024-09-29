package edu.handles.tables;

import edu.handles.commands.Command;

import java.util.LinkedHashMap;


public class CommandTable {
    private LinkedHashMap<String, Command> commandHashMap = new LinkedHashMap<>();

    public CommandTable(Command... commands) {
        for (Command command : commands) {
            commandHashMap.put(command.getCommandName(), command);
        }
    }

    public CommandTable(CommandTable preCommandTable, Command... additionalCommands) {
        this.commandHashMap.putAll(preCommandTable.getCommands());

        for (Command command : additionalCommands) {
            commandHashMap.put(command.getCommandName(), command);
        }
    }

    public void addCommandsFrom(CommandTable otherTable) {
        commandHashMap.putAll(otherTable.getCommands());
    }

    public LinkedHashMap<String, Command> getCommands() {
        return new LinkedHashMap<>(commandHashMap);
    }
}
