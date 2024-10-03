package edu.handles.commands.enteties;


import edu.Data.DataManager;
import edu.Data.dto.ClientTransfer;
import edu.handles.commands.Command;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class RegisterCommand implements Command {
    private final DataManager dataManager;

    public RegisterCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    Logger logger = Logger.getLogger(RegisterCommand.class.getName());

    @Override
    public SendMessage execute(Update update) {
        // Извлечение данных пользователя из объекта Update
        Long tgUserId = update.getMessage().getFrom().getId();
        String name = update.getMessage().getFrom().getFirstName();

        // Ответ пользователю по умолчанию
        SendMessage response = new SendMessage();
        response.setChatId(update.getMessage().getChatId().toString());

        // Создание объекта User

        try {
            // Попытка зарегистрировать пользователя в БД
            if(dataManager.isUserExists(tgUserId)){
                response.setText("Данный этап регистрации успешно пройден. Если хотите просмотреть свой профиль, нажмите /profile");

                return response;
            }
            else {
                ClientTransfer clientProfile = getClientTransfer(tgUserId, name);
                dataManager.addUser(clientProfile);
                response.setText("Вам необходимо подтвердить номер телефона для дальнейшего использования сервиса.");

                KeyboardButton contactButton = new KeyboardButton("Отправить номер телефона");
                contactButton.setRequestContact(true);
                KeyboardRow keyboardRow = new KeyboardRow();
                keyboardRow.add(contactButton);

                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                keyboardMarkup.setKeyboard(Arrays.asList(keyboardRow));
                keyboardMarkup.setResizeKeyboard(true);  // Подстройка клавиатуры под экран
                keyboardMarkup.setOneTimeKeyboard(true);  // Клавиатура исчезнет после нажатия

                response.setReplyMarkup(keyboardMarkup);


                List<ClientTransfer> l = dataManager.getAllUsers();
                logger.info("Кол-во пользователей - " + l.size());
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setText("Произошла ошибка при регистрации. Пожалуйста, попробуйте позже.");
        } catch (Exception e) {
            e.printStackTrace();
            response.setText("Произошла ошибка при регистрации. Пожалуйста, попробуйте позже.");

        }

        return response;
    }

    private static ClientTransfer getClientTransfer(Long tgUserId, String name) {
        Date currentDate = new Date(System.currentTimeMillis());
        ClientTransfer clientProfile = new ClientTransfer(
                null, // id, можно оставить null, так как он сгенерируется автоматически
                tgUserId, // Telegram User ID
                null, // Телефон, если неизвестен, можно оставить null
                name, // Имя пользователя
                currentDate, // Дата последнего визита (текущая дата)
                null, // VPN-профиль, если отсутствует
                false, // Статус VPN-профиля, можно оставить null
                null // Дата истечения, если неизвестна
        );
        return clientProfile;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        // Эта команда может быть видимой в пользовательской клавиатуре
        return true;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        if(status == UserProfileStatus.GUEST) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String getCommandName() {
        return "/register";
    }

    @Override
    public String getCommandDescription() {
        return "Регистрирует нового пользователя в системе.";
    }
}