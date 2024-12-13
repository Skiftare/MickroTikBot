package edu.handles.commands.enteties;


import edu.Data.JdbcDataManager;
import edu.Data.dto.ClientTransfer;
import edu.handles.commands.BotResponseToUserWrapper;
import edu.handles.commands.Command;
import edu.handles.commands.UserMessageFromBotWrapper;
import edu.models.UserProfileStatus;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class RegisterCommand implements Command {
    private final JdbcDataManager jdbcDataManager;
    Logger logger = Logger.getLogger(RegisterCommand.class.getName());

    public RegisterCommand(JdbcDataManager jdbcDataManager) {
        this.jdbcDataManager = jdbcDataManager;
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
                new Date(0) //Пускай так
        );
        return clientProfile;
    }

    @Override
    public BotResponseToUserWrapper execute(UserMessageFromBotWrapper update) {
        // Извлечение данных пользователя из объекта Update
        Long tgUserId = update.userId();
        String name = update.firstName();

        // Ответ пользователю по умолчанию
        String response;


        try {
            // Попытка зарегистрировать пользователя в БД
            if (jdbcDataManager.isUserExists(tgUserId)) {
                response = ("Для подтверждения телефона воспользуйтесь кнопкой ниже");
            } else {
                ClientTransfer clientProfile = getClientTransfer(tgUserId, name);
                jdbcDataManager.addUser(clientProfile);
                response = ("Вам необходимо подтвердить номер телефона для дальнейшего использования сервиса.");


                List<ClientTransfer> l = jdbcDataManager.getAllUsers();
                logger.info("Кол-во пользователей - " + l.size());
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
            response = ("Произошла ошибка при регистрации. Пожалуйста, попробуйте позже.");
        }
        KeyboardButton contactButton = new KeyboardButton("Отправить номер телефона");
        contactButton.setRequestContact(true);
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(contactButton);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(List.of(keyboardRow));
        keyboardMarkup.setResizeKeyboard(true);  // Подстройка клавиатуры под экран
        keyboardMarkup.setOneTimeKeyboard(true);  // Клавиатура исчезнет после нажатия



        return new BotResponseToUserWrapper(tgUserId, response, false, keyboardMarkup);
    }

    @Override
    public boolean isVisibleForKeyboard() {
        // Эта команда может быть видимой в пользовательской клавиатуре
        return true;
    }

    @Override
    public boolean isVisibleForKeyboard(UserProfileStatus status) {
        return (status == UserProfileStatus.GUEST || status == UserProfileStatus.UNCONFIRMED);
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
