package com.studybox.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public final class DbUtil {

    private static final String PROPERTIES_FILE = "db.properties";
    private static final Properties PROPERTIES = loadProperties();

    private DbUtil() {
    }

    public static Connection getConnection() throws SQLException {
        String driver = PROPERTIES.getProperty("jdbc.driverClassName");
        String url = PROPERTIES.getProperty("jdbc.url");
        String username = PROPERTIES.getProperty("jdbc.username");
        String password = PROPERTIES.getProperty("jdbc.password");

        try {
            if (driver != null && !driver.isBlank()) {
                Class.forName(driver);
            }
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver: " + driver, ex);
        }

        return DriverManager.getConnection(url, username, password);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE)) {
            if (Objects.isNull(inputStream)) {
                throw new IllegalStateException("Missing " + PROPERTIES_FILE + " in classpath.");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load database configuration.", ex);
        }
    }
}
