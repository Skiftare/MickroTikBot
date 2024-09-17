package Commands;

import Configuration.TelegramBotConfig;

public class HelpCommand implements Command {

    @Override
    public void execute(TelegramBotConfig bot, long chatId) {
        // Сообщение с перечнем доступных команд
        String helpMessage = "Доступные команды:\n" +
                "/start - начать работу с ботом\n" +
                "/help - получить список команд\n" +
                "/about - основная информация о боте";
        bot.sendMessage(chatId, helpMessage);  // Отправляем сообщение с помощью бота
    }
}