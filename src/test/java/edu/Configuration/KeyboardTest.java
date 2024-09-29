package edu.Configuration;

import edu.handles.commands.Command;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class KeyboardTest {

    @Test
    @DisplayName("Test that KeyboardMarkupBuilder builds the correct keyboard")
    public void testThatKeyboardMarkupBuilderBuildsTheCorrectKeyboard() {

        Command command1 = new CommandMock("Command1", true);
        Command command2 = new CommandMock("Command2", true);
        Command command3 = new CommandMock("Command3", false);

        List<Command> commands = Arrays.asList(command1, command2, command3);

        KeyboardMarkupBuilder keyboardBuilder = new KeyboardMarkupBuilder(commands);

        ReplyKeyboardMarkup keyboardMarkup = keyboardBuilder.build();

        assertEquals(2, keyboardMarkup.getKeyboard().size());

        KeyboardRow firstRow = keyboardMarkup.getKeyboard().get(0);
        KeyboardRow secondRow = keyboardMarkup.getKeyboard().get(1);
        assertEquals("Command1", firstRow.get(0).getText());
        assertEquals("Command2", secondRow.get(0).getText());
    }

    private record CommandMock(String name, boolean visible) implements Command {

        @Override
            public SendMessage execute(Update update) {
                return null;
            }

            @Override
            public boolean isVisibleForKeyboard() {
                return visible;
            }

            @Override
            public String getCommandName() {
                return name;
            }

            @Override
            public String getCommandDescription() {
                return "";
            }
        }
}
