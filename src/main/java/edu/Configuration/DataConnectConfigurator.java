package edu.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnectConfigurator {
    private static final String URL = "jdbc:postgresql://postgresql:5432/table";
    private static final String USER = System.getenv("NEW_DATABASE_LOGIN");
    private static final String PASSWORD = System.getenv("NEW_DATABASE_PASS");


    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
