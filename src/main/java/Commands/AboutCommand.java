package Commands;

import Configuration.TelegramBotConfig;

public class AboutCommand implements Command {

    @Override
    public void execute(TelegramBotConfig bot, long chatId) {
        // Сообщение с информацией о боте
        String aboutMessage = "Это Telegram бот, написанный двумя студентами МатМех-а в рамках практики курса ООП.";
        bot.sendMessage(chatId, aboutMessage);  // Отправляем сообщение
    }
}