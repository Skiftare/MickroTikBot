package edu.handles.commands.enteties;

import edu.Integrations.server.Connector;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class GetConnectionCommand implements Command {

    private final Connector connector;

    public GetConnectionCommand(Connector connector) {
        this.connector = connector;
    }

    public SendMessage execute(Update update) {
        // Генерация профиля с помощью реализации Connector
        String profile = connector.generateProfile();

        // Получаем идентификатор чата
        Long chatId = update.getMessage().getChatId();

        // Создаем сообщение с профилем для пользователя
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Your VPN configuration: " + profile);


            // Отправляем сообщение пользователю
          //  sender.execute(message);
            return message;

    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return false;
    }

    @Override
    public String getCommandName() {
        return "/get_connection";
    }

    @Override
    public String getCommandDescription() {
        return "Возвращает конфигурацию VPN-подключения.";
    }
}

