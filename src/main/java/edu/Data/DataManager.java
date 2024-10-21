package edu.Data;

import edu.Data.dto.ClientTransfer;
import edu.models.UserProfileStatus;

import java.time.Duration;
import java.util.List;

public interface DataManager {
    // Сохранение нового пользователя
    void save(ClientTransfer client);

    // Поиск пользователя по Telegram ID
    ClientTransfer findById(Long tgUserId);

    // Проверка, существует ли пользователь с данным Telegram ID
    boolean isUserExists(Long tgUserId);

    // Обновление данных пользователя
    void update(ClientTransfer client);

    // Удаление пользователя по Telegram ID
    void deleteById(Long tgUserId);

    // Получение списка всех пользователей
    List<ClientTransfer> getAllUsers();
    void updateUserPhoneByTelegramId(Long tgUserId, String phoneNumber);

    // Получение статуса профиля пользователя
    UserProfileStatus getUserProfileStatus(Long tgUserId);
    boolean extendVpnProfile(Long tgUserId, Duration duration);
}