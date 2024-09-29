package edu.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
    private static final String URL = "jdbc:postgresql://postgresql:5432/table";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";


    public  Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

