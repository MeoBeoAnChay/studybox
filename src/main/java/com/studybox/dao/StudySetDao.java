package com.studybox.dao;

import com.studybox.model.StudySet;
import com.studybox.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudySetDao {

        private static final String INSERT_SQL =
            "INSERT INTO study_sets (user_id, name, description, subject, status) "
            + "VALUES (?, ?, ?, ?, ?)";

        private static final String UPDATE_SQL =
            "UPDATE study_sets "
            + "SET name = ?, description = ?, subject = ?, status = ?, updated_at = CURRENT_TIMESTAMP "
            + "WHERE id = ? AND user_id = ?";

        private static final String DELETE_SQL =
            "DELETE FROM study_sets "
            + "WHERE id = ? AND user_id = ?";

        private static final String SELECT_BY_ID_SQL =
            "SELECT id, user_id, name, description, subject, status, created_at, updated_at "
            + "FROM study_sets "
            + "WHERE id = ? AND user_id = ? "
            + "LIMIT 1";

        private static final String SELECT_ALL_BY_USER_SQL =
            "SELECT id, user_id, name, description, subject, status, created_at, updated_at "
            + "FROM study_sets "
            + "WHERE user_id = ? "
            + "ORDER BY updated_at DESC, id DESC";

        private static final String SELECT_RECENT_BY_USER_SQL =
            "SELECT id, user_id, name, description, subject, status, created_at, updated_at "
            + "FROM study_sets "
            + "WHERE user_id = ? "
            + "ORDER BY updated_at DESC, id DESC "
            + "LIMIT ?";

        private static final String COUNT_BY_USER_SQL =
            "SELECT COUNT(*) AS total "
            + "FROM study_sets "
            + "WHERE user_id = ?";

        private static final String COUNT_BY_USER_AND_STATUS_SQL =
            "SELECT COUNT(*) AS total "
            + "FROM study_sets "
            + "WHERE user_id = ? AND status = ?";

        private static final String COUNT_BY_USER_AND_NAME_SQL =
            "SELECT COUNT(*) AS total "
            + "FROM study_sets "
            + "WHERE user_id = ? AND name = ?";

        private static final String COUNT_BY_USER_AND_NAME_EXCLUDING_ID_SQL =
            "SELECT COUNT(*) AS total "
            + "FROM study_sets "
            + "WHERE user_id = ? AND name = ? AND id <> ?";

    public long create(StudySet studySet) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, studySet.getUserId());
            statement.setString(2, studySet.getName());
            statement.setString(3, studySet.getDescription());
            statement.setString(4, studySet.getSubject());
            statement.setString(5, studySet.getStatus());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    studySet.setId(id);
                    return id;
                }
            }

            throw new SQLException("Creating study set failed, no ID returned.");
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to create study set.", ex);
        }
    }

    public boolean update(StudySet studySet) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, studySet.getName());
            statement.setString(2, studySet.getDescription());
            statement.setString(3, studySet.getSubject());
            statement.setString(4, studySet.getStatus());
            statement.setLong(5, studySet.getId());
            statement.setLong(6, studySet.getUserId());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to update study set.", ex);
        }
    }

    public boolean delete(long id, long userId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setLong(1, id);
            statement.setLong(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to delete study set.", ex);
        }
    }

    public Optional<StudySet> findById(long id, long userId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setLong(1, id);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to load study set.", ex);
        }
    }

    public List<StudySet> findAllByUser(long userId) {
        return queryList(SELECT_ALL_BY_USER_SQL, statement -> statement.setLong(1, userId));
    }

    public List<StudySet> findRecentByUser(long userId, int limit) {
        return queryList(SELECT_RECENT_BY_USER_SQL, statement -> {
            statement.setLong(1, userId);
            statement.setInt(2, limit);
        });
    }

    public long countByUser(long userId) {
        return count(COUNT_BY_USER_SQL, statement -> statement.setLong(1, userId));
    }

    public long countByUserAndStatus(long userId, String status) {
        return count(COUNT_BY_USER_AND_STATUS_SQL, statement -> {
            statement.setLong(1, userId);
            statement.setString(2, status);
        });
    }

    public boolean existsByUserAndName(long userId, String name, Long excludeId) {
        String sql = excludeId == null ? COUNT_BY_USER_AND_NAME_SQL : COUNT_BY_USER_AND_NAME_EXCLUDING_ID_SQL;
        return count(sql, statement -> {
            statement.setLong(1, userId);
            statement.setString(2, name);
            if (excludeId != null) {
                statement.setLong(3, excludeId);
            }
        }) > 0;
    }

    private List<StudySet> queryList(String sql, StatementConfigurer configurer) {
        List<StudySet> studySets = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            configurer.configure(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    studySets.add(map(resultSet));
                }
            }
            return studySets;
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to query study sets.", ex);
        }
    }

    private long count(String sql, StatementConfigurer configurer) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            configurer.configure(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("total");
                }
                return 0L;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to count study sets.", ex);
        }
    }

    private StudySet map(ResultSet resultSet) throws SQLException {
        StudySet studySet = new StudySet();
        studySet.setId(resultSet.getLong("id"));
        studySet.setUserId(resultSet.getLong("user_id"));
        studySet.setName(resultSet.getString("name"));
        studySet.setDescription(resultSet.getString("description"));
        studySet.setSubject(resultSet.getString("subject"));
        studySet.setStatus(resultSet.getString("status"));
        studySet.setCreatedAt(resultSet.getTimestamp("created_at"));
        studySet.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return studySet;
    }

    @FunctionalInterface
    private interface StatementConfigurer {
        void configure(PreparedStatement statement) throws SQLException;
    }
}
