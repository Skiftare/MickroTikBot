package edu.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnectConfigurator {
    private final String url;
    private final String user;
    private final String password;

    // Конструктор для тестов или другой конфигурации
    public DataConnectConfigurator(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // Конструктор для продакшн-использования (по умолчанию через env)
    public DataConnectConfigurator() {
        this.url = "jdbc:postgresql://postgresql:5432/table"; // или из env если нужно
        this.user = System.getenv("NEW_DATABASE_LOGIN");
        this.password = System.getenv("NEW_DATABASE_PASS");
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
