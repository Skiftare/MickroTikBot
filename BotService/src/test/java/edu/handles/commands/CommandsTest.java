package edu.handles.commands;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import edu.handles.commands.enteties.*;
import edu.handles.tables.CommandTable;
import edu.models.UserProfileStatus;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandsTest {

    @Test
    @DisplayName("Test that InfoCommand is executed and returned the correct SendMessage")
    public void testThatInfoCommandIsExecutedAndReturnedCorrectSendMessage() {
        Command infoCommand = new InfoCommand();
        UserMessageFromBotWrapper wrapper = new UserMessageFromBotWrapper(
            1L, // userId
            "/info", // userMessage
            UserProfileStatus.GUEST, // status
            "TestUser", // firstName
            "TestLastName" // lastName
        );
        
        BotResponseToUserWrapper result = infoCommand.execute(wrapper);

        assertNotNull(result.message());
        assertEquals("/info", infoCommand.getCommandName());
        assertEquals("Этот бот помогает установить безопасное приватное VPN-соединение, что особенно полезно для повышения уровня конфиденциальности в общественных местах, таких как кофейни.", result.message());
    }

    @Test
    @DisplayName("Test that AuthorsCommand is executed and returned the correct SendMessage")
    public void testThatAuthorsCommandIsExecutedAndReturnedCorrectSendMessage() {
        Command authorsCommand = new AuthorsCommand();
        UserMessageFromBotWrapper wrapper = new UserMessageFromBotWrapper(
            1L,
            "/authors",
            UserProfileStatus.GUEST,
            "TestUser",
            "TestLastName"
        );

        BotResponseToUserWrapper result = authorsCommand.execute(wrapper);

        assertNotNull(result.message(), "SendMessage should not be null");
        assertEquals("/authors", authorsCommand.getCommandName());
        assertEquals("artem", result.message());
    }

    @Test
    @DisplayName("Test that HelpCommand is executed and returned the correct SendMessage")
    public void testThatHelpCommandIsExecutedWithMockedCommandTableAndReturnedCorrectSendMessage() {
        CommandTable mockedCommandTable = Mockito.mock(CommandTable.class);
        when(mockedCommandTable.getCommands()).thenReturn(new LinkedHashMap<>());

        Command helpCommand = new HelpCommand(mockedCommandTable);
        UserMessageFromBotWrapper wrapper = new UserMessageFromBotWrapper(
            1L,
            "/help",
            UserProfileStatus.GUEST,
            "TestUser",
            "TestLastName"
        );

        BotResponseToUserWrapper result = helpCommand.execute(wrapper);

        assertNotNull(result.message());
        assertEquals("/help", helpCommand.getCommandName());
        verify(mockedCommandTable, times(UserProfileStatus.values().length)).getCommands();
    }

}