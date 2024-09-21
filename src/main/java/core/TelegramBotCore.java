package core;


import handles.commands.Command;
import handles.tables.CommandTable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TelegramBotCore extends TelegramLongPollingBot {

    private final String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    private final String UNKNOWN_COMMAND = "Неизвестная команда. Введите /help для списка доступных команд.";

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
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = commandTable.values().stream()
                .filter(Command::isVisibleForKeyboard)
                .map(command -> {
                    KeyboardRow row = new KeyboardRow();
                    row.add(new KeyboardButton(command.getCommandName()));
                    return row;
                })
                .collect(Collectors.toList());

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public void sendMessageToUser(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}

