import core.TelegramBotCore;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotApplication {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            // Регистрация Telegram-бота в API
            botsApi.registerBot(new TelegramBotCore());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}