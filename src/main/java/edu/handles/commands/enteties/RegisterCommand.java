package edu.handles.commands.enteties;

import edu.Data.DataManager;
import edu.Data.dto.Dto_user;
import edu.handles.commands.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
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
        Dto_user newDtouser = new Dto_user(null, tgUserId, null, name, null, null);

        try {
            // Попытка зарегистрировать пользователя в БД
            if(dataManager.isUserExists(tgUserId)){
                response.setText("Вы уже зарегистрированы в системе.");
                return response;
            }
            else {
                dataManager.addUser(newDtouser);
                response.setText("Вы успешно зарегистрированы в системе.");
                List<Dto_user> l = dataManager.getAllUsers();
                logger.info("Кол-во пользователей - " + l.size());
            }
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
            response.setText("Произошла ошибка при регистрации. Пожалуйста, попробуйте позже.");
        }

        return response;
    }

    @Override
    public boolean isVisibleForKeyboard() {
        // Эта команда может быть видимой в пользовательской клавиатуре
        return true;
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