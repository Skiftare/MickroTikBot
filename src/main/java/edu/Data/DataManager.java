package edu.Data;


import edu.Configuration.DataConnectConfigurator;
import edu.Data.dto.ClientTransfer;
import edu.models.UserProfileStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

//TODO: пускай мы будем хранить лишь зарегестрированных пользователей БД,
// а тех, кто пока только проходят регистрацию,
// будем держать в памяти (LinkedHashMap какая-то подойдёт)
@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})
public class DataManager {
    private final DataConnectConfigurator dataConnection;
    private static final String INSERT_USER_QUERY =

            "INSERT INTO users "
                    + "(tg_user_id, phone, name, user_last_visited, vpn_profile, is_vpn_profile_alive, expired_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID_QUERY =
            "SELECT * FROM users WHERE tg_user_id = ?";
    private static final String UPDATE_USER_PHONE_QUERY =
            "UPDATE users SET phone = ? WHERE tg_user_id = ?";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE tg_user_id = ?";



    public DataManager(DataConnectConfigurator dataConnection) {
        this.dataConnection = dataConnection;
    }


    // Метод для создания записи
    public void save(ClientTransfer client) {

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_QUERY)) {

            preparedStatement.setLong(1, client.tgUserId());
            preparedStatement.setString(2, client.phone());
            preparedStatement.setString(3, client.name());
            preparedStatement.setDate(4, new java.sql.Date(client.userLastVisited().getTime()));
            preparedStatement.setString(5, client.vpnProfile());
            preparedStatement.setBoolean(6, client.isVpnProfileAlive());
            preparedStatement.setDate(7, new java.sql.Date(client.expiredAt().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }

    // Метод для чтения записи
    public ClientTransfer findById(Long tgUserId) {

        ClientTransfer client = null;

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID_QUERY)) {

            preparedStatement.setLong(1, tgUserId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                client = new ClientTransfer(
                        resultSet.getLong("id"),
                        resultSet.getLong("tg_user_id"),
                        resultSet.getString("phone"),
                        resultSet.getString("name"),
                        resultSet.getDate("user_last_visited"),
                        resultSet.getString("vpn_profile"),
                        resultSet.getBoolean("is_vpn_profile_alive"),
                        resultSet.getDate("expired_at")
                );
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
        return client;
    }

    public boolean isUserExists(Long tgUserId) {

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID_QUERY)) {

            preparedStatement.setLong(1, tgUserId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Если запрос вернет хотя бы одну строку, пользователь существует
            return resultSet.next();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public void addUser(ClientTransfer client) {


        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_QUERY)) {

            preparedStatement.setLong(1, client.tgUserId());
            preparedStatement.setString(2, client.phone());
            preparedStatement.setString(3, client.name());
            preparedStatement.setDate(4, new java.sql.Date(client.userLastVisited().getTime()));
            preparedStatement.setString(5, client.vpnProfile());
            preparedStatement.setBoolean(6, client.isVpnProfileAlive());
            preparedStatement.setDate(7, new java.sql.Date(client.expiredAt().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }


    // Метод для обновления записи
    public void update(ClientTransfer client) {
        String query = "UPDATE users SET "
                + "phone = ?, name = ?, "
                + "user_last_visited = ?, vpn_profile = ?, is_vpn_profile_alive = ?, expired_at = ? "
                + "WHERE tg_user_id = ?";

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, client.phone());
            preparedStatement.setString(2, client.name());
            preparedStatement.setDate(3, new java.sql.Date(client.userLastVisited().getTime()));
            preparedStatement.setString(4, client.vpnProfile());
            preparedStatement.setBoolean(5, client.isVpnProfileAlive());
            preparedStatement.setDate(6, new java.sql.Date(client.expiredAt().getTime()));
            preparedStatement.setLong(7, client.tgUserId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void updateUserPhoneByTelegramId(Long tgUserId, String newPhone) {
        Logger.getAnonymousLogger().info("Updating phone for user with tg_user_id: " + tgUserId);
        Logger.getAnonymousLogger().info("Updating phone for user with tg_user_id: " + tgUserId);
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_PHONE_QUERY)) {

            // Устанавливаем параметры для запроса
            preparedStatement.setString(1, newPhone);
            preparedStatement.setLong(2, tgUserId);

            // Выполняем обновление
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                Logger.getAnonymousLogger().info("User phone updated successfully for tg_user_id: " + tgUserId);
            } else {
                Logger.getAnonymousLogger().info("No user found with tg_user_id: " + tgUserId);
            }

        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }


    // Метод для удаления записи
    public void deleteById(Long tgUserId) {
        String query = "DELETE FROM users WHERE tg_user_id = ?";

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, tgUserId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }

    // Метод для получения всех записей
    public List<ClientTransfer> getAllUsers() {
        List<ClientTransfer> clients = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ClientTransfer client = new ClientTransfer(
                        resultSet.getLong("id"),
                        resultSet.getLong("tg_user_id"),
                        resultSet.getString("phone"),
                        resultSet.getString("name"),
                        resultSet.getDate("user_last_visited"),
                        resultSet.getString("vpn_profile"),
                        resultSet.getBoolean("is_vpn_profile_alive"),
                        resultSet.getDate("expired_at")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
        return clients;
    }

    public UserProfileStatus getUserProfileStatus(Long tgUserId) {

        ClientTransfer client = findById(tgUserId);
        if (client == null) {
            return UserProfileStatus.GUEST;
        } else if (client.phone() == null) {
            return UserProfileStatus.UNCONFIRMED;
        } else if (client.isVpnProfileAlive()) {
            return UserProfileStatus.ACTIVE_VPN;
        } else {
            return UserProfileStatus.NO_VPN;
        }
    }
}
