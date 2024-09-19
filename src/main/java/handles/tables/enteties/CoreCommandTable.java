package handles.tables.enteties;

import handles.tables.CommandTable;


import handles.commands.Command;
import handles.commands.enteties.HelpCommand;
import handles.commands.enteties.InfoCommand;

import java.util.HashMap;

public class CoreCommandTable implements CommandTable {
    private HashMap<String, Command> commandHashMap = new HashMap<>();

    public CoreCommandTable() {
        Command helpCommand = new HelpCommand();
        commandHashMap.put(helpCommand.getCommandName(), helpCommand);
        commandHashMap.putAll(new PreCommandTable().getCommands());
    }

    @Override
    public HashMap<String, Command> getCommands() {
        return new HashMap<>(commandHashMap);
    }
}