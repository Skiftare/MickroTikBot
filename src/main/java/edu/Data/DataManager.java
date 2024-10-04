package edu.Data;


import edu.Configuration.DataConnectConfigurator;
import edu.Data.dto.ClientTransfer;
import edu.models.UserProfileStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DataManager {
    private final DataConnectConfigurator dataConnection;

    public DataManager(DataConnectConfigurator dataConnection) {
        this.dataConnection = dataConnection;
    }


    // Метод для создания записи
    public void save(ClientTransfer client) {
        String query = "INSERT INTO users (tg_user_id, phone, name, user_last_visited, vpn_profile, is_vpn_profile_alive, expired_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, client.tgUserId());
            preparedStatement.setString(2, client.phone());
            preparedStatement.setString(3, client.name());
            preparedStatement.setDate(4, new java.sql.Date(client.userLastVisited().getTime()));
            preparedStatement.setString(5, client.vpnProfile());
            preparedStatement.setBoolean(6, client.isVpnProfileAlive());
            preparedStatement.setDate(7, new java.sql.Date(client.expiredAt().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для чтения записи
    public ClientTransfer findById(Long tgUserId) {
        String query = "SELECT * FROM users WHERE tg_user_id = ?";
        ClientTransfer client = null;

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

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
            e.printStackTrace();
        }
        return client;
    }
    public boolean isUserExists(Long tgUserId) {
        String query = "SELECT 1 FROM users WHERE tg_user_id = ?";

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, tgUserId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Если запрос вернет хотя бы одну строку, пользователь существует
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addUser(ClientTransfer client) throws Exception {

        String query = "INSERT INTO users (tg_user_id, phone, name, user_last_visited, vpn_profile, is_vpn_profile_alive, expired_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, client.tgUserId());
            preparedStatement.setString(2, client.phone());
            preparedStatement.setString(3, client.name());
            preparedStatement.setDate(4, new java.sql.Date(client.userLastVisited().getTime()));
            preparedStatement.setString(5, client.vpnProfile());
            preparedStatement.setBoolean(6, client.isVpnProfileAlive());
            preparedStatement.setDate(7, new java.sql.Date(client.expiredAt().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Метод для обновления записи
    public void update(ClientTransfer client) {
        String query = "UPDATE users SET phone = ?, name = ?, user_last_visited = ?, vpn_profile = ?, is_vpn_profile_alive = ?, expired_at = ? WHERE tg_user_id = ?";

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
            e.printStackTrace();
        }
    }
    public void updateUserPhoneByTelegramId(Long tgUserId, String newPhone) {
        String query = "UPDATE users SET phone = ? WHERE tg_user_id = ?";

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Устанавливаем параметры для запроса
            preparedStatement.setString(1, newPhone);
            preparedStatement.setLong(2, tgUserId);

            // Выполняем обновление
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User phone updated successfully for tg_user_id: " + tgUserId);
            } else {
                System.out.println("No user found with tg_user_id: " + tgUserId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return clients;
    }
    public UserProfileStatus getUserProfileStatus(Long tgUserId) {
        String query = "SELECT 1 FROM users WHERE tg_user_id = ?";
        UserProfileStatus status = UserProfileStatus.GUEST;

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, tgUserId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if(resultSet.getString("phone") == null) {
                    status = UserProfileStatus.UNCONFIRMED;
                } else if(resultSet.getBoolean("is_vpn_profile_alive")) {
                    status = UserProfileStatus.ACTIVE_VPN;
                } else {
                    status = UserProfileStatus.NO_VPN;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
}
