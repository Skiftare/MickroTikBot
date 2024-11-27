package edu.Integrations.telegram;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Logger;

public class SubscriptionChecker extends DefaultAbsSender {
    private static final Logger LOGGER = Logger.getLogger(SubscriptionChecker.class.getName());
    private final String botToken;

    public SubscriptionChecker(String botToken) {
        super(new DefaultBotOptions());
        this.botToken = botToken;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public boolean isUserSubscribed(Long userId, String channelId) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(channelId);
        getChatMember.setUserId(userId);

        try {
            ChatMember chatMember = execute(getChatMember);

            // Проверяем, является ли пользователь участником или администратором канала
            String status = chatMember.getStatus();
            return status.equals("member") || status.equals("administrator") || status.equals("creator");
        } catch (TelegramApiException e) {
            LOGGER.warning("Ошибка при проверке подписки пользователя: " + e.getMessage());
            return false;
        }
    }
}
