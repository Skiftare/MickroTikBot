package edu.handles.commands;


import edu.handles.commands.Command;
import edu.handles.commands.enteties.AuthorsCommand;
import edu.handles.commands.enteties.HelpCommand;
import edu.handles.commands.enteties.InfoCommand;
import edu.handles.tables.CommandTable;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandsTest {

    @Test
    @DisplayName("Test that InfoCommand is executed and returned the correct SendMessage")
    public void testThatInfoCommandIsExecutedAndReturnedCorrectSendMessage() {
        Command infoCommand = new InfoCommand();
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getChatId()).thenReturn(1L);
        SendMessage result = infoCommand.execute(update);

        assertNotNull(result.getText());
        assertEquals("/info", infoCommand.getCommandName());
        assertEquals("Этот бот помогает установить безопасное приватное VPN-соединение, что особенно полезно для повышения уровня конфиденциальности в общественных местах, таких как кофейни.", result.getText());
    }

    @Test
    @DisplayName("Test that AuthorsCommand is executed and returned the correct SendMessage")
    public void testThatAuthorsCommandIsExecutedAndReturnedCorrectSendMessage() {
        Command authorsCommand = new AuthorsCommand();
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getChatId()).thenReturn(1L);
        SendMessage result = authorsCommand.execute(update);

        assertNotNull(result.getText(), "SendMessage should not be null");
        assertEquals("/authors", authorsCommand.getCommandName());
        assertEquals("Kirill Bratanov\nArtem Berdichevskii", result.getText());
    }

    @Test
    @DisplayName("Test that HelpCommand is executed and returned the correct SendMessage")
    public void testThatHelpCommandIsExecutedWithMockedCommandTableAndReturnedCorrectSendMessage() {
        // Мокаем CommandTable
        CommandTable mockedCommandTable = Mockito.mock(CommandTable.class);

        // Мокаем вызов метода getCommands()
        when(mockedCommandTable.getCommands()).thenReturn(new HashMap<>());

        Command helpCommand = new HelpCommand(mockedCommandTable);

        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getChatId()).thenReturn(1L);

        SendMessage result = helpCommand.execute(update);

        assertNotNull(result.getText());
        assertEquals("/help", helpCommand.getCommandName());
        verify(mockedCommandTable, times(1)).getCommands(); // Проверяем, что getCommands был вызван один раз
    }
}