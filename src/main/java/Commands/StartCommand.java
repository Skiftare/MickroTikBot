package Commands;

import Configuration.TelegramBotConfig;

public class StartCommand implements Command {

    @Override
    public void execute(TelegramBotConfig bot, long chatId) {
        // Приветственное сообщение для пользователя
        String welcomeMessage = "Добро пожаловать в MikroTikBot!";
        bot.sendMessage(chatId, welcomeMessage);  // Отправляем сообщение пользователю
    }
}

