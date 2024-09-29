package edu.Data;

import edu.Configuration.DataConnection;
import edu.Data.dto.Dto_user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DataManager {
    private final DataConnection dataConnection;

    public DataManager(DataConnection dataConnection) {
        this.dataConnection = dataConnection;
    }


    // Статический метод для добавления пользователя
    public void addUser(Dto_user dtouser) throws SQLException, ClassNotFoundException {
        String SQL = "INSERT INTO users (tg_user_id, phone, name, vpn_profile, expired_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setLong(1, dtouser.tgUserId());
            pstmt.setString(2, dtouser.phone());
            pstmt.setString(3, dtouser.name());
            pstmt.setString(4, dtouser.vpnProfile());
            pstmt.setDate(5, dtouser.expiredAt());
            pstmt.executeUpdate();
        }
    }
    public boolean isUserExists(Long tgUserId) throws SQLException, ClassNotFoundException {
        String SQL = "SELECT * FROM users WHERE tg_user_id = ?";
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setLong(1, tgUserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }

    }

    // Статический метод для поиска пользователя по tg_user_id
    public Dto_user getUserByTgUserId(Long tgUserId) throws SQLException, ClassNotFoundException {
        String SQL = "SELECT * FROM users WHERE tg_user_id = ?";
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setLong(1, tgUserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Dto_user(
                            rs.getLong("id"),
                            rs.getLong("tg_user_id"),
                            rs.getString("phone"),
                            rs.getString("name"),
                            rs.getString("vpn_profile"),
                            rs.getDate("expired_at")
                    );
                }
            }
        }
        return null;
    }

    // Статический метод для обновления пользователя
    public void updateUser(Dto_user dtouser) throws SQLException {
        String SQL = "UPDATE users SET phone = ?, name = ?, vpn_profile = ?, expired_at = ? WHERE tg_user_id = ?";
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, dtouser.phone());
            pstmt.setString(2, dtouser.name());
            pstmt.setString(3, dtouser.vpnProfile());
            pstmt.setDate(4, dtouser.expiredAt());
            pstmt.setLong(5, dtouser.tgUserId());
            pstmt.executeUpdate();
        }
    }

    // Статический метод для удаления пользователя по tg_user_id
    public void deleteUser(Long tgUserId) throws SQLException, ClassNotFoundException {
        String SQL = "DELETE FROM users WHERE tg_user_id = ?";
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setLong(1, tgUserId);
            pstmt.executeUpdate();
        }
    }

    // Статический метод для получения всех пользователей
    public List<Dto_user> getAllUsers() throws SQLException, ClassNotFoundException {
        String SQL = "SELECT * FROM users";
        List<Dto_user> dtousers = new ArrayList<>();
        try (Connection connection = dataConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                dtousers.add(new Dto_user(
                        rs.getLong("id"),
                        rs.getLong("tg_user_id"),
                        rs.getString("phone"),
                        rs.getString("name"),
                        rs.getString("vpn_profile"),
                        rs.getDate("expired_at")
                ));
            }
        }
        return dtousers;
    }
}
