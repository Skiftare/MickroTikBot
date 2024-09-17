package Commands;

import Configuration.TelegramBotConfig;

public interface Command {
    // Метод для выполнения команды. Принимает объект бота и ID чата
    void execute(TelegramBotConfig bot, long chatId);
}

