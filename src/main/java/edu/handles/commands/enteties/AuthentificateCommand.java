package edu.handles.commands.enteties;


import edu.Data.DataManager;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.logging.Logger;

import static edu.Integrations.server.CryptoGenerator.generateUsersHash;

public class AuthentificateCommand implements Command {
    private final DataManager jdbcDataManager;

    public AuthentificateCommand(DataManager incomeDataManger) {
        this.jdbcDataManager = incomeDataManger;
    }


    @Override
    public SendMessage execute(Update update) {
        Long chatId = update.getMessage().getChatId();

        if (update.getMessage().hasContact()) {
            Contact contact = update.getMessage().getContact();
            String phoneNumber = contact.getPhoneNumber();
            Logger.getAnonymousLogger().info("Client " + chatId + " sent phone number: " + phoneNumber);
            // Здесь ваш код для обработки номера телефона и аутентификации
            String userHash = generateUsersHash(update);
            String responseText;
            try {
                jdbcDataManager.updateUserPhoneAndHash(chatId, phoneNumber, userHash);
                Logger.getAnonymousLogger().info("User " + chatId
                        + " was successfully authenticated with " + userHash + " hash");
                responseText = "Вы успешно аутентифицированы!";

            } catch (Exception e) {
                responseText = "Что-то пошло не так. Скорее всего, БД не отвечает. Попробуйте позже.";
            }
            return new SendMessage(chatId.toString(), responseText);
        }

        return new SendMessage(chatId.toString(), "Что-то пошло не так");
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;  // Эта команда не будет видна на клавиатуре
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return false;
    }

    @Override
    public String getCommandName() {
        return "/authentificate";  // Название команды
    }

    @Override
    public String getCommandDescription() {
        return "Аутентификация пользователя по номеру телефона";  // Описание команды
    }
}
