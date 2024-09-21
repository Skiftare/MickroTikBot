package edu.Configuration;


import edu.handles.commands.Command;
import edu.handles.tables.CommandTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TelegramBotCore extends TelegramLongPollingBot {

    private static final String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    private static final String UNKNOWN_COMMAND = "Неизвестная команда. Введите /help для списка доступных команд.";

    private final Map<String, Command> commandTable = new HashMap<>();

    public TelegramBotCore(CommandTable coreCommandTable) {
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
            SendMessage response = new SendMessage();
            if (command != null) {
                response = command.execute(update);
            } else {
                response.setChatId(chatId);
                response.setText(UNKNOWN_COMMAND);
            }
            response.setReplyMarkup(getKeyboardMarkup());
            sendMessageToUser(response);
        }


    }


    private ReplyKeyboardMarkup getKeyboardMarkup() {
        // Передаем список команд в сборщик
        KeyboardMarkupBuilder keyboardBuilder = new KeyboardMarkupBuilder(new ArrayList<>(commandTable.values()));
        return keyboardBuilder.build();
    }

    public void sendMessageToUser(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            Logger.getAnonymousLogger().severe("Error while sending message to user: " + e.getMessage());

        }
    }


}

