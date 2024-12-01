package edu.handles.commands;

import java.sql.Date;
import java.util.LinkedHashMap;

import static edu.Integrations.chr.RouterConnector.initialisationSecret;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import edu.Configuration.SSHConnection;
import edu.Data.dto.ClientTransfer;
import edu.Integrations.chr.RouterConnector;
import edu.handles.commands.enteties.*;
import edu.handles.tables.CommandTable;
import edu.models.UserProfileStatus;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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
        
        SendMessage result = infoCommand.execute(wrapper);

        assertNotNull(result.getText());
        assertEquals("/info", infoCommand.getCommandName());
        assertEquals("Этот бот помогает установить безопасное приватное VPN-соединение, что особенно полезно для повышения уровня конфиденциальности в общественных местах, таких как кофейни.", result.getText());
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

        SendMessage result = authorsCommand.execute(wrapper);

        assertNotNull(result.getText(), "SendMessage should not be null");
        assertEquals("/authors", authorsCommand.getCommandName());
        assertEquals("artem", result.getText());
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

        SendMessage result = helpCommand.execute(wrapper);

        assertNotNull(result.getText());
        assertEquals("/help", helpCommand.getCommandName());
        verify(mockedCommandTable, times(1)).getCommands();
    }


    @Test
    @DisplayName("Тест, что команда ProfileCommand выполняется и возвращает правильный SendMessage")
    public void testThatProfileCommandIsExecutedAndReturnsCorrectSendMessage() {
        Command profileCommand = new ProfileCommand();
        UserMessageFromBotWrapper wrapper = new UserMessageFromBotWrapper(
            1L,
            "/chr_profile",
            UserProfileStatus.ACTIVE_VPN,
            "TestUser",
            "TestLastName"
        );

        try (MockedStatic<RouterConnector> mockedSecretInitialiser = Mockito.mockStatic(RouterConnector.class)) {
            mockedSecretInitialiser.when(() -> initialisationSecret(
                    new ClientTransfer(1L,"","",new Date(0)," ",false,new Date(1)))).thenReturn("Secret initialized!");

            SendMessage result = profileCommand.execute(wrapper);

            assertNotNull(result.getText());
            assertEquals("/chr_profile", profileCommand.getCommandName());
        }
    }
}