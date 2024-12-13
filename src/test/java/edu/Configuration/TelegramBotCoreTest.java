package edu.Configuration;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.handles.commands.UserMessageFromBotWrapper;
import edu.handles.commands.enteties.AuthentificateCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import edu.Data.JdbcDataManager;
import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.Command;
import edu.handles.tables.CommandTable;
import edu.models.UserProfileStatus;

class TelegramBotCoreTest {
    private TelegramBotCore bot;
    private JdbcDataManager mockJdbcDataManager;
    private CommandTable mockCommandTable;
    private KeyboardMarkupBuilder mockKeyboardBuilder;
    private Command mockCommand;

    @BeforeEach
    void setUp() {
        mockJdbcDataManager = mock(JdbcDataManager.class);
        mockCommandTable = mock(CommandTable.class);
        mockKeyboardBuilder = mock(KeyboardMarkupBuilder.class);
        mockCommand = mock(Command.class);

        // Создаем LinkedHashMap вместо SingletonMap
        LinkedHashMap<String, Command> commandMap = new LinkedHashMap<>();
        commandMap.put("/test", mockCommand);

        // Настраиваем поведение моков
        when(mockKeyboardBuilder.getKeyboardByStatus(any())).thenReturn(new ReplyKeyboardMarkup());
        when(mockCommandTable.getCommands()).thenReturn(commandMap);
        
        // Создаем тестируемый объект
        bot = spy(new TelegramBotCore(mockCommandTable, mockKeyboardBuilder, mockJdbcDataManager));
    }

    @Test
    void testHandleKnownCommand() throws TelegramApiException {
        // Подготовка
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123L);
        message.setChat(chat);
        message.setText("/test");
        update.setMessage(message);

        when(mockJdbcDataManager.getUserProfileStatus(123L)).thenReturn(UserProfileStatus.ACTIVE_VPN);
        when(mockCommand.isVisibleForKeyboard(any())).thenReturn(true);
        when(mockCommand.execute(any())).thenReturn(
            new BotResponseToUserWrapper(123L, "Test response", false, null)
        );

        // Выполнение
        bot.onUpdateReceived(update);

        // Проверка
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        
        SendMessage sentMessage = argumentCaptor.getValue();
        assertEquals("123", sentMessage.getChatId());
        assertEquals("Test response", sentMessage.getText());
        assertNotNull(sentMessage.getReplyMarkup());
    }

    @Test
    void testHandleUnknownCommand() throws TelegramApiException {
        // Подготовка
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123L);
        message.setChat(chat);
        message.setText("/unknown");
        update.setMessage(message);

        when(mockJdbcDataManager.getUserProfileStatus(123L)).thenReturn(UserProfileStatus.GUEST);

        // Выполнение
        bot.onUpdateReceived(update);

        // Проверка
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        
        SendMessage sentMessage = argumentCaptor.getValue();
        assertEquals("123", sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("Неизвестная команда"));
    }

    @Test
    void testHandleContactMessage() throws TelegramApiException {
        // Подготовка
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        Contact contact = new Contact();
        
        chat.setId(123L);
        contact.setPhoneNumber("+1234567890");
        message.setChat(chat);
        message.setContact(contact);
        update.setMessage(message);

        // Создаем мок команды аутентификации
        Command authCommand = mock(Command.class);
        
        // Создаем новую мапу команд с командой аутентификации
        LinkedHashMap<String, Command> commandMap = new LinkedHashMap<>();
        commandMap.put("/authentificate", authCommand);
        
        // Обновляем мок commandTable
        when(mockCommandTable.getCommands()).thenReturn(commandMap);
        
        // Пересоздаем бота с обновленной мапой команд
        bot = spy(new TelegramBotCore(mockCommandTable, mockKeyboardBuilder, mockJdbcDataManager));

        when(mockJdbcDataManager.getUserProfileStatus(123L)).thenReturn(UserProfileStatus.UNCONFIRMED);
        when(authCommand.execute(any())).thenReturn(
            new BotResponseToUserWrapper(123L, "Auth success", false, null)
        );

        // Выполнение
        bot.onUpdateReceived(update);

        // Проверка
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        
        SendMessage sentMessage = argumentCaptor.getValue();
        assertEquals("123", sentMessage.getChatId());
        assertEquals("Auth success", sentMessage.getText());
        
        // Проверяем, что команда аутентификации была вызвана
        verify(authCommand).execute(any(UserMessageFromBotWrapper.class));
    }

    @Test
    void testSendMessageWithMarkdown() throws TelegramApiException {
        // Подготовка
        BotResponseToUserWrapper response = new BotResponseToUserWrapper(
            123L,
            "Test *bold* message",
            true,
            new ReplyKeyboardMarkup()
        );

        // Выполнение
        bot.sendMessageToUser(response);

        // Проверка
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        
        SendMessage sentMessage = argumentCaptor.getValue();
        assertEquals("123", sentMessage.getChatId());
        assertEquals("Test *bold* message", sentMessage.getText());
        //TODO: assertTrue(sentMessage.isEnableMarkdown()); - не работает, т.к. нет метода.
    }

    @Test
    void testHandleMessageWithSpaces() throws TelegramApiException {
        // Подготовка
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123L);
        message.setChat(chat);
        message.setText("/test with additional parameters");
        update.setMessage(message);

        when(mockJdbcDataManager.getUserProfileStatus(123L)).thenReturn(UserProfileStatus.ACTIVE_VPN);
        when(mockCommand.isVisibleForKeyboard(any())).thenReturn(true);
        when(mockCommand.execute(any())).thenReturn(
            new BotResponseToUserWrapper(123L, "Test response", false, null)
        );

        // Выполнение
        bot.onUpdateReceived(update);

        // Проверка
        verify(mockCommand).execute(any());
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        
        SendMessage sentMessage = argumentCaptor.getValue();
        assertEquals("Test response", sentMessage.getText());
    }
} 