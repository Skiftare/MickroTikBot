package edu.Configuration;


import edu.handles.commands.Command;
import edu.handles.tables.CommandTable;
import edu.models.UserProfileStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.google.common.collect.LinkedHashMultimap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KeyboardMarkupBuilderTest {




    @Test
    @DisplayName("Test that Guest keyboard is generated with the correct commands")
    public void testThatGuestKeyboardIsGeneratedWithCorrectCommands() {
        // Мокаем команды для статуса Guest
        CommandTable mockedCommandTable = Mockito.mock(CommandTable.class);

        LinkedHashMap<String, Command> commandMap = new LinkedHashMap<>();
        Command mockCommand = Mockito.mock(Command.class);
        when(mockCommand.isVisibleForKeyboard(UserProfileStatus.GUEST)).thenReturn(true);
        when(mockCommand.getCommandName()).thenReturn("/start");
        commandMap.put("/start", mockCommand);

        when(mockedCommandTable.getCommands()).thenReturn(commandMap);
        KeyboardMarkupBuilder keyboardMarkupBuilder = new KeyboardMarkupBuilder(mockedCommandTable);
        // Генерируем клавиатуру
        ReplyKeyboardMarkup result = keyboardMarkupBuilder.getKeyboardByStatus(UserProfileStatus.GUEST);

        // Проверяем корректность клавиатуры
        assertNotNull(result);
        List<KeyboardRow> rows = result.getKeyboard();
        assertEquals(1, rows.size()); // Проверяем, что есть одна строка
        assertEquals(1, rows.get(0).size()); // В строке одна кнопка
        assertEquals("/start", rows.get(0).get(0).getText()); // Проверяем, что кнопка имеет корректный текст
    }

    @Test
    @DisplayName("Test that Unconfirmed keyboard is generated with the correct commands")
    public void testThatUnconfirmedKeyboardIsGeneratedWithCorrectCommands() {
        CommandTable mockedCommandTable = Mockito.mock(CommandTable.class);

        // Мокаем команды для статуса Unconfirmed
        LinkedHashMap<String, Command> commandMap = new LinkedHashMap<>();
        Command mockCommand = Mockito.mock(Command.class);
        when(mockCommand.isVisibleForKeyboard(UserProfileStatus.UNCONFIRMED)).thenReturn(true);
        when(mockCommand.getCommandName()).thenReturn("/confirm");
        commandMap.put("/confirm", mockCommand);

        when(mockedCommandTable.getCommands()).thenReturn(commandMap);
        KeyboardMarkupBuilder keyboardMarkupBuilder = new KeyboardMarkupBuilder(mockedCommandTable);

        // Генерируем клавиатуру
        ReplyKeyboardMarkup result = keyboardMarkupBuilder.getKeyboardByStatus(UserProfileStatus.UNCONFIRMED);

        // Проверяем корректность клавиатуры
        assertNotNull(result);
        List<KeyboardRow> rows = result.getKeyboard();
        assertEquals(1, rows.size());
        assertEquals(1, rows.get(0).size());
        assertEquals("/confirm", rows.get(0).get(0).getText());
    }

    @Test
    @DisplayName("Test that ActiveVPN keyboard is generated with the correct commands")
    public void testThatActiveVPNKeyboardIsGeneratedWithCorrectCommands() {

        CommandTable mockedCommandTable = Mockito.mock(CommandTable.class);
// Мокаем команды для статуса ActiveVPN
        LinkedHashMap<String, Command> commandMap = new LinkedHashMap<>();
        Command mockCommand = Mockito.mock(Command.class);
        when(mockCommand.isVisibleForKeyboard(UserProfileStatus.ACTIVE_VPN)).thenReturn(true);
        when(mockCommand.getCommandName()).thenReturn("/disconnect");
        commandMap.put("/disconnect", mockCommand);

        when(mockedCommandTable.getCommands()).thenReturn(commandMap);
        KeyboardMarkupBuilder keyboardMarkupBuilder = new KeyboardMarkupBuilder(mockedCommandTable);

        // Генерируем клавиатуру
        ReplyKeyboardMarkup result = keyboardMarkupBuilder.getKeyboardByStatus(UserProfileStatus.ACTIVE_VPN);

        // Проверяем корректность клавиатуры
        assertNotNull(result);
        List<KeyboardRow> rows = result.getKeyboard();
        assertEquals(1, rows.size());
        assertEquals(1, rows.get(0).size());
        assertEquals("/disconnect", rows.get(0).get(0).getText());
    }

    @Test
    @DisplayName("Test that NoVPN keyboard is generated with the correct commands")
    public void testThatNoVPNKeyboardIsGeneratedWithCorrectCommands() {
        CommandTable mockedCommandTable = Mockito.mock(CommandTable.class);

        // Мокаем команды для статуса NoVPN
        LinkedHashMap<String, Command> commandMap = new LinkedHashMap<>();
        Command mockCommand = Mockito.mock(Command.class);
        when(mockCommand.isVisibleForKeyboard(UserProfileStatus.NO_VPN)).thenReturn(true);
        when(mockCommand.getCommandName()).thenReturn("/connect");
        commandMap.put("/connect", mockCommand);

        when(mockedCommandTable.getCommands()).thenReturn(commandMap);
        KeyboardMarkupBuilder keyboardMarkupBuilder = new KeyboardMarkupBuilder(mockedCommandTable);

        // Генерируем клавиатуру
        ReplyKeyboardMarkup result = keyboardMarkupBuilder.getKeyboardByStatus(UserProfileStatus.NO_VPN);

        // Проверяем корректность клавиатуры
        assertNotNull(result);
        List<KeyboardRow> rows = result.getKeyboard();
        assertEquals(1, rows.size());
        assertEquals(1, rows.get(0).size());
        assertEquals("/connect", rows.get(0).get(0).getText());
    }
}

