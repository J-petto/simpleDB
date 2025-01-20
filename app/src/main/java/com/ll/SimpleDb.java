package com.ll;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;

@Getter
@Setter
public class SimpleDb {

    private String url;
    private String username;
    private String password;

    private boolean devMode;

    private final Connection connection;

    public SimpleDb(String url, String username, String password, String db) {
        this.url = String.format("jdbc:mysql://%s:3306/%s", url, db);
        this.username = username;
        this.password = password;

        this.connection = getConnection(this.url, username, password);
    }

    private Connection getConnection(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create the database connection", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Sql genSql() {
        return new Sql(connection);
    }

    public void run(String query) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(String query, Object... parameters) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {

        }
    }

    public void startTransaction() {
    }

    public void rollback() {
    }

    public void commit() {
    }
}
