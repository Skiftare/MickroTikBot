package edu.handles.commands.enteties;


import edu.Data.DataManager;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AuthentificateCommand implements Command {
    private final DataManager dataManager;

    public AuthentificateCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public SendMessage execute(Update update) {
        Long chatId = update.getMessage().getChatId();

        if (update.getMessage().hasContact()) {
            Contact contact = update.getMessage().getContact();
            String phoneNumber = contact.getPhoneNumber();

            // Здесь ваш код для обработки номера телефона и аутентификации
            String responseText = "Вы успешно аутентифицированы!";
            dataManager.updateUserPhoneByTelegramId(
                    update.getMessage().getChatId(),
                    phoneNumber
            );
            return new SendMessage(chatId.toString(), responseText);
        }

        return new SendMessage(chatId.toString(), "Для аутентификации необходимо отправить номер телефона.");
    }

    @Override
    public boolean isVisibleForKeyboard() {
        return false;  // Эта команда не будет видна на клавиатуре
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        if(status == UserProfileStatus.UNCONFIRMED || status == UserProfileStatus.GUEST) {
            return true;
        }
        else{
            return false;
        }
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