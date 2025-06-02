package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var sql = "CREATE TABLE users" +
                "(id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var statement2 = conn.prepareStatement(sql2)) {
                statement2.setString(1, "tommy");
                statement2.setString(2, "123456789");
                statement2.executeUpdate();

                statement2.setString(1, "alex");
                statement2.setString(2, "333333333");
                statement2.executeUpdate();
            }

            var sql3 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }

            var sql4 = "DELETE FROM users WHERE username = ?";
            try (var statement4 = conn.prepareStatement(sql4)) {
                statement4.setString(1, "tommy");
                var rowsDeleted = statement4.executeUpdate();
                System.out.println("Records deleted: " + rowsDeleted);
            }
        }
    }
}
