
package com.cts.outward.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private DatabaseConnection() {
    }

    static {
        try {
            Class.forName(DatabaseConfig.DRIVER);

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(
                    "PostgreSQL JDBC Driver not found",
                    e
            );
        }
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USERNAME,
                DatabaseConfig.PASSWORD
        );
    }
}

