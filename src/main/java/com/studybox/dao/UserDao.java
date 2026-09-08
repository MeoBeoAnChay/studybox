package com.studybox.dao;

import com.studybox.model.User;
import com.studybox.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDao {

        private static final String INSERT_SQL =
            "INSERT INTO users (full_name, email, password_hash, role, status) "
            + "VALUES (?, ?, ?, ?, ?)";

        private static final String SELECT_BY_EMAIL_SQL =
            "SELECT id, full_name, email, password_hash, role, status, created_at, updated_at "
            + "FROM users "
            + "WHERE email = ? "
            + "LIMIT 1";

        private static final String SELECT_BY_ID_SQL =
            "SELECT id, full_name, email, password_hash, role, status, created_at, updated_at "
            + "FROM users "
            + "WHERE id = ? "
            + "LIMIT 1";

    public long create(User user) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getStatus());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    user.setId(id);
                    return id;
                }
            }

            throw new SQLException("Creating user failed, no ID returned.");
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to create user.", ex);
        }
    }

    public Optional<User> findByEmail(String email) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL_SQL)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to load user by email.", ex);
        }
    }

    public Optional<User> findById(long id) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to load user by id.", ex);
        }
    }

    private User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPasswordHash(resultSet.getString("password_hash"));
        user.setRole(resultSet.getString("role"));
        user.setStatus(resultSet.getString("status"));
        user.setCreatedAt(resultSet.getTimestamp("created_at"));
        user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return user;
    }
}
