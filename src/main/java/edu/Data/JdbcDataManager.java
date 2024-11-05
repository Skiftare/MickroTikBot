package edu.Data;


import edu.Configuration.DataConnectConfigurator;
import edu.Data.PaymentDataManager.TransactionRecord;
import edu.Data.dto.ClientTransfer;
import edu.Data.dto.TransactionRecord;
import edu.Data.dto.UserInfo;
import edu.models.UserProfileStatus;
import javassist.compiler.ast.Pair;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import edu.Data.dto.TransactionRecord;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;


@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})
public class JdbcDataManager implements DataManager,PaymentDataManager {
    private final DataConnectConfigurator dataConnection;
    private static final String INSERT_USER_QUERY =

            "INSERT INTO users "
                    + "(tg_user_id, phone, name, user_last_visited, vpn_profile, is_vpn_profile_alive, expired_at, is_payment_pending, key_for_recognizing, balance) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?)";
    private static final String SELECT_USER_BY_ID_QUERY =
            "SELECT * FROM users WHERE tg_user_id = ?";
    private static final String UPDATE_USER_PHONE_QUERY =
            "UPDATE users SET phone = ? WHERE tg_user_id = ?";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE tg_user_id = ?";
    private static final String UPDATE_USER_PHONE_AND_HASH_QUERY =
            "UPDATE users SET phone = ?, key_for_recognizing = ? WHERE tg_user_id = ?";



    public JdbcDataManager(DataConnectConfigurator dataConnection) {
        this.dataConnection = dataConnection;
    }

    @Override

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
            preparedStatement.setBoolean(8, client.isInPaymentProcess());
            preparedStatement.setString(9, client.paymentKey());
            preparedStatement.setBigDecimal(10, client.balance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }
    @Override

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
                        resultSet.getDate("expired_at"),
                        resultSet.getBoolean("is_payment_pending"),
                        resultSet.getString("key_for_recognizing"),
                        resultSet.getBigDecimal("balance")
                );
            }
            else{
                Logger.getAnonymousLogger().info("No user found with tg_user_id: " + tgUserId);
                client=new ClientTransfer(
                        -1L,
                        -1L,
                        "-",
                        null,
                        new java.sql.Date(0),
                        "not found",
                        false,
                        new java.sql.Date(0),
                        false,
                        "0",
                        BigDecimal.ZERO
                );
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
        return client;
    }
    @Override
    public UserInfo getInfoById(Long tgUserId) {
        ClientTransfer client = findById(tgUserId);
        UserProfileStatus userProfileStatus;
        if (client == null) {
            userProfileStatus = UserProfileStatus.GUEST;
        } else if (client.phone() == null) {
            userProfileStatus = UserProfileStatus.UNCONFIRMED;
        } else if (client.isInPaymentProcess()) {
            userProfileStatus = UserProfileStatus.ACTIVE_PAYMENT;
        } else if (client.isVpnProfileAlive()) {
            userProfileStatus = UserProfileStatus.ACTIVE_VPN;
        } else {
            userProfileStatus = UserProfileStatus.NO_VPN;
        }
        return new UserInfo(client, userProfileStatus);
    }

    @Override

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
    @Override
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
            preparedStatement.setBoolean(8, client.isInPaymentProcess());
            preparedStatement.setString(9, client.paymentKey());
            preparedStatement.setBigDecimal(10, client.balance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override

    // Метод для обновления записи
    public void update(ClientTransfer client) {
        String query = "UPDATE users SET "
                + "phone = ?, name = ?, "
                + "user_last_visited = ?, "
                + "vpn_profile = ?, "
                + "is_vpn_profile_alive = ?, "
                + "expired_at = ?,"
                + "is_payment_pending = ?,"
                + "key_for_recognizing = ?,"
                + "balance = ? "
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
            preparedStatement.setBoolean(8, client.isInPaymentProcess());
            preparedStatement.setString(9, client.paymentKey());
            preparedStatement.setBigDecimal(10, client.balance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
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


    @Override
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

    @Override
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
                        resultSet.getDate("expired_at"),
                        resultSet.getBoolean("is_payment_pending"),
                        resultSet.getString("key_for_recognizing"),
                        resultSet.getBigDecimal("balance")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
        return clients;
    }

    @Override
    public UserProfileStatus getUserProfileStatus(Long tgUserId) {

        ClientTransfer client = findById(tgUserId);

        if (client == null) {
            return UserProfileStatus.GUEST;
        } else if (client.phone() == null) {
            return UserProfileStatus.UNCONFIRMED;
        } else if (client.isInPaymentProcess()) {
            return UserProfileStatus.ACTIVE_PAYMENT;
        } else if (client.isVpnProfileAlive()) {
            return UserProfileStatus.ACTIVE_VPN;
        } else {
            return UserProfileStatus.NO_VPN;
        }
    }
    @Override
    public void setPaymentProcessStatus(Long tgUserId, boolean status) {
        String query = "UPDATE users SET is_payment_pending = ? WHERE tg_user_id = ?";
        UserProfileStatus userProfileStatus = getUserProfileStatus(tgUserId);
        if (userProfileStatus == UserProfileStatus.GUEST || userProfileStatus == UserProfileStatus.UNCONFIRMED) {
            Logger.getAnonymousLogger().info("User not found with tg_user_id: " + tgUserId);
            throw new IllegalArgumentException("Only confirmed users can by VPN" + tgUserId);
        }
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, status);
            preparedStatement.setLong(2, tgUserId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }
    @Override
    public boolean extendVpnProfile(Long tgUserId, Duration duration) {
        String query = "UPDATE users SET expired_at = ?, is_vpn_profile_alive = true WHERE tg_user_id = ?";
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ClientTransfer client = findById(tgUserId);
            if (client == null) {
                Logger.getAnonymousLogger().info("User not found with tg_user_id: " + tgUserId);
                return false;
            }

            LocalDate newExpiryDate;
            Date currentExpiryDate = client.expiredAt();
            LocalDate today = LocalDate.now();

            if (currentExpiryDate == null || 
                currentExpiryDate.getTime() == 0 || 
                currentExpiryDate.equals(new Date(0))) {
                newExpiryDate = today.plusDays(duration.toDays());
                Logger.getAnonymousLogger().info("Starting from today due to null or epoch date");
            } else {
                LocalDate currentExpiryLocalDate = new java.sql.Date(currentExpiryDate.getTime()).toLocalDate();
                if (currentExpiryLocalDate.isBefore(today)) {
                    newExpiryDate = today.plusDays(duration.toDays());
                    Logger.getAnonymousLogger().info("Starting from today due to expired date");
                } else {
                    newExpiryDate = currentExpiryLocalDate.plusDays(duration.toDays());
                    Logger.getAnonymousLogger().info("Extending from current expiry date");
                }
            }

            preparedStatement.setDate(1, java.sql.Date.valueOf(newExpiryDate));
            preparedStatement.setLong(2, tgUserId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                Logger.getAnonymousLogger().info("VPN profile extended successfully for tg_user_id: " + tgUserId);
                return true;
            } else {
                Logger.getAnonymousLogger().info("No user found with tg_user_id: " + tgUserId);
                return false;
            }

        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    @Override
    public void updateUserPhoneAndHash(Long tgUserId, String newPhone, String hash) {
        Logger.getAnonymousLogger().info("Updating phone and hash for user with tg_user_id: " + tgUserId);
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_PHONE_AND_HASH_QUERY)) {

            preparedStatement.setString(1, newPhone);
            preparedStatement.setString(2, hash);
            preparedStatement.setLong(3, tgUserId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                Logger.getAnonymousLogger().info("User phone and hash updated successfully for tg_user_id: " + tgUserId);
            } else {
                Logger.getAnonymousLogger().info("No user found with tg_user_id: " + tgUserId);
            }

        } catch (SQLException e) {
            Logger.getAnonymousLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void addIncomingTransaction(PaymentOperationResponse paymentOperation){

        TransactionRecord transaction = TransactionRecord(
                paymentOperation.getTransactionHash(),
                paymentOperation.getTransaction().get().getMemo().toString(),
                new BigDecimal(paymentOperation.getAmount()),
                paymentOperation.getSourceAccount()
        );

    }
    

}
