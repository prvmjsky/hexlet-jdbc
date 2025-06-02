package io.hexlet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void createUserBase() throws SQLException {
        var sql = "CREATE TABLE users" +
            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
        try (var stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void save(User user) throws SQLException {
        var id = user.getId();

        if (id == null) {
            var sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getPhone());
                stmt.executeUpdate();

                var keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            var sql = "UPDATE users SET username = ?, phone = ? WHERE id = ?";
            try (var stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getPhone());
                stmt.setLong(3, id);
                stmt.executeUpdate();
            }
        }
    }

    public void deleteByID(Long id) throws SQLException {
        var sql = "DELETE FROM users WHERE id = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var recordsDeleted = stmt.executeUpdate();
            System.out.println("Records deleted: " + recordsDeleted);
        }
    }

    public void deleteByName(String name) throws SQLException {
        var sql = "DELETE FROM users WHERE username = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            var recordsDeleted = stmt.executeUpdate();
            System.out.println("Records deleted: " + recordsDeleted);
        }
    }

    public Optional<User> findByID(Long id) throws SQLException {
        var sql = "SELECT * FROM users WHERE id = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var result = stmt.executeQuery();

            if (result.next()) {
                var name = result.getString("username");
                var phone = result.getString("phone");
                var user = new User(name, phone);
                user.setId(id);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        }
    }

    public void showAll() throws SQLException {
        var sql = "SELECT * FROM users";
        try (var stmt = connection.createStatement()) {
            var result = stmt.executeQuery(sql);

            if (!result.isBeforeFirst()) {
                System.out.println("Userbase is empty");
            }

            while (result.next()) {
                System.out.println(result.getString("username"));
                System.out.println(result.getString("phone"));
            }
        }
    }
}
