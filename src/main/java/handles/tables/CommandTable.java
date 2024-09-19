package handles.tables;

import handles.commands.Command;

import java.util.HashMap;
import java.util.Map;

public interface CommandTable {
    HashMap<String, Command> getCommands();

}
