package Configuration;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import Commands.Command;
import Commands.StartCommand;
import Commands.HelpCommand;
import Commands.AboutCommand;

import java.util.HashMap;
import java.util.Map;

public class TelegramBotConfig extends TelegramLongPollingBot {

    // Таблица команд: ключ — команда, значение — объект класса, реализующего команду
    private final Map<String, Command> commandTable = new HashMap<>();

    public TelegramBotConfig() {
        // Регистрация команд в таблице
        commandTable.put("/start", new StartCommand());   // Команда "/start" будет выполнять StartCommand
        commandTable.put("/help", new HelpCommand());     // Команда "/help" будет выполнять HelpCommand
        commandTable.put("/about", new AboutCommand());     // Команда "/about" будет выполнять AboutCommand
    }

    @Override
    public String getBotUsername() {
        return "MikroTikMatMechBot";  // Здесь указывается имя вашего бота
    }

    @Override
    public String getBotToken() {
        return "7863985403:AAE_dERZUduDIwcpVPG5lo7aK3wa_2XVwo0";   // Здесь указывается токен вашего бота, полученный у BotFather
    }

    // Метод, который вызывается, когда бот получает новое сообщение
    @Override
    public void onUpdateReceived(Update update) {
        // Проверяем, содержит ли сообщение текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();  // Текст сообщения
            long chatId = update.getMessage().getChatId();       // ID чата, куда нужно отправить ответ

            // Проверяем, есть ли введённая команда в таблице команд
            Command command = commandTable.get(messageText);
            if (command != null) {
                // Если команда найдена, выполняем её
                command.execute(this, chatId);
            } else {
                // Если команда не найдена, отправляем сообщение об ошибке
                sendMessage(chatId, "Неизвестная команда. Введите /help для списка доступных команд.");
            }
        }
    }

    // Вспомогательный метод для отправки сообщений пользователям
    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);  // Указываем ID чата
        message.setText(text);      // Указываем текст сообщения
        try {
            execute(message);       // Отправляем сообщение
        } catch (TelegramApiException e) {
            e.printStackTrace();    // Выводим ошибку, если сообщение не удалось отправить
        }
    }
}
