import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import Configuration.TelegramBotConfig;

public class BotApplication {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            // Регистрация Telegram-бота в API
            botsApi.registerBot(new TelegramBotConfig());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}