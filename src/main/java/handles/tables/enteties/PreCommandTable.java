package handles.tables.enteties;

import handles.commands.Command;
import handles.commands.enteties.AuthorsCommand;
import handles.commands.enteties.InfoCommand;
import handles.tables.CommandTable;

import java.util.HashMap;

public class PreCommandTable implements CommandTable {

    private HashMap<String, Command> commandHashMap = new HashMap<>();

    public PreCommandTable() {
        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();
        commandHashMap.put(infoCommand.getCommandName(), infoCommand);
        commandHashMap.put(authorsCommand.getCommandName(), authorsCommand);
    }

    @Override
    public HashMap<String, Command> getCommands() {
        return new HashMap<>(commandHashMap);
    }
}
