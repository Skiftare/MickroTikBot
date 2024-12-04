package edu.Configuration;

import edu.Data.JdbcDataManager;
import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.handles.tables.CommandTable;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TelegramBotCore extends TelegramLongPollingBot {

    private static final String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    private static final String UNKNOWN_COMMAND = "Неизвестная команда. Введите /help для списка доступных команд "
            + "или зарегистрируйтесь (/register).";
    private final KeyboardMarkupBuilder keyboardMarkupBuilder;
    private final Map<String, Command> commandTable = new HashMap<>();
    private final JdbcDataManager jdbcDataManager;

    public TelegramBotCore(CommandTable coreCommandTable,
                           KeyboardMarkupBuilder keyboardMarkupBuilder,
                           JdbcDataManager incomingJdbcDataManager) {
        this.keyboardMarkupBuilder = keyboardMarkupBuilder;
        this.jdbcDataManager = incomingJdbcDataManager;
        commandTable.putAll(coreCommandTable.getCommands());
    }

    @Override
    public String getBotUsername() {
        return "MikroTikMatMechBot";
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().split(" ")[0];
            long chatId = update.getMessage().getChatId();

            Command command = commandTable.get(messageText);

            UserProfileStatus status = jdbcDataManager.getUserProfileStatus(chatId);
            Logger.getAnonymousLogger().info("Client "
                    + chatId
                    + " with status "
                    + status.toString()
                    + " sent message: "
                    + messageText);

            UserMessageFromBotWrapper userMessage = new UserMessageFromBotWrapper(update, status);
            BotResponseToUserWrapper response = new BotResponseToUserWrapper(chatId, UNKNOWN_COMMAND, false, null);
            if (command != null && command.isVisibleForKeyboard(status)) {
                response = command.execute(userMessage);
                if (response.keyboardMarkup() == null) {
                    response = new BotResponseToUserWrapper(
                            response.userId(),
                            response.message(),
                            response.isMarkdownEnabled(),
                            getKeyboardMarkup(status));
                }
            } else {
                if (response.keyboardMarkup() == null) {
                    response = new BotResponseToUserWrapper(
                            response.userId(),
                            response.message(),
                            response.isMarkdownEnabled(),
                            getKeyboardMarkup(status));
                }

            }
            sendMessageToUser(response);
        } else if (update.getMessage().hasContact()) {
            Logger.getAnonymousLogger().info("User "
                    + update.getMessage().getChatId()
                    + " sent phone number: "
                    + update.getMessage().getContact().getPhoneNumber());

            UserProfileStatus status = jdbcDataManager.getUserProfileStatus(update.getMessage().getChatId());

            Command authentificateCommand = commandTable.get("/authentificate");
            UserMessageFromBotWrapper userMessage = new UserMessageFromBotWrapper(update, status);
            BotResponseToUserWrapper response = authentificateCommand.execute(userMessage);
            Long chatId = update.getMessage().getChatId();
            status = jdbcDataManager.getUserProfileStatus(chatId);
            response = new BotResponseToUserWrapper(
                    response.userId(),
                    response.message(),
                    response.isMarkdownEnabled(),
                    getKeyboardMarkup(status));

            sendMessageToUser(response);
        }
    }


    private ReplyKeyboardMarkup getKeyboardMarkup(UserProfileStatus status) {
        return keyboardMarkupBuilder.getKeyboardByStatus(status);
    }

    public void sendMessageToUser(BotResponseToUserWrapper message) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(message.message());
            sendMessage.setChatId(message.userId());
            sendMessage.setReplyMarkup(message.keyboardMarkup());
            sendMessage.enableMarkdown(message.isMarkdownEnabled());
            execute(sendMessage);
        } catch (TelegramApiException e) {
            Logger.getAnonymousLogger().severe("Error while sending message to user: " + e.getMessage());
        }
    }


}
