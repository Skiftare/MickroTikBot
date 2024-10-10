package edu.handles.commands;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import edu.Configuration.SSHConnection;
import edu.handles.commands.enteties.AuthorsCommand;
import edu.handles.commands.enteties.HelpCommand;
import edu.handles.commands.enteties.InfoCommand;
import edu.handles.commands.enteties.StateCommand;
import edu.handles.tables.CommandTable;

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
        assertEquals("skif\nartem", result.getText());
    }

    @Test
    @DisplayName("Test that HelpCommand is executed and returned the correct SendMessage")
    public void testThatHelpCommandIsExecutedWithMockedCommandTableAndReturnedCorrectSendMessage() {
        // Мокаем CommandTable
        CommandTable mockedCommandTable = Mockito.mock(CommandTable.class);

        // Мокаем вызов метода getCommands()
        when(mockedCommandTable.getCommands()).thenReturn(new LinkedHashMap<>());

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

    @Test
    @DisplayName("Test that StateCommand is executed and returned the correct SendMessage")
    public void testThatStateCommandIsExecutedAndReturnedCorrectSendMessage() {
        Command stateCommand = new StateCommand();
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getChatId()).thenReturn(1L);

        try (MockedStatic<SSHConnection> mockedSSHConnection = Mockito.mockStatic(SSHConnection.class)) {
            mockedSSHConnection.when(SSHConnection::establishingSSH).thenReturn("SSH connection established");

            SendMessage result = stateCommand.execute(update);

            assertNotNull(result.getText(), "SendMessage should not be null");
            assertEquals("/state", stateCommand.getCommandName());
            assertEquals("SSH connection established", result.getText());
        }
    }
}