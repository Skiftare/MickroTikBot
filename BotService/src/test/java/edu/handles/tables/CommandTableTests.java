package edu.handles.tables;


import edu.handles.commands.Command;
import edu.handles.commands.enteties.AuthorsCommand;
import edu.handles.commands.enteties.HelpCommand;
import edu.handles.commands.enteties.InfoCommand;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandTableTests {


    @Test
    @DisplayName("Test that CommandTable handles multiple commands and returned correct size")
    public void testThatCommandTableHandlesMultipleCommandsAndReturnedCorrectSize() {
        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();

        CommandTable commandTable = new CommandTable(infoCommand, authorsCommand);

        assertEquals(2, commandTable.getCommands().size());
        assertTrue(commandTable.getCommands().containsKey("/info"));
        assertTrue(commandTable.getCommands().containsKey("/authors"));
    }

    @Test
    @DisplayName("Test that CommandTable adds commands from another table and returned correct size")
    public void testThatCommandTableAddsCommandsFromAnotherTableAndReturnedCorrectSize() {
        Command infoCommand = new InfoCommand();
        Command authorsCommand = new AuthorsCommand();
        Command helpCommand = new HelpCommand(new CommandTable());

        CommandTable commandTable = new CommandTable(infoCommand, authorsCommand);
        CommandTable anotherTable = new CommandTable(helpCommand);

        commandTable.addCommandsFrom(anotherTable);

        assertEquals(3, commandTable.getCommands().size());
        assertTrue(commandTable.getCommands().containsKey("/help"));
    }
}