package com.disaster.analysis.infrastructure.database;

import com.disaster.analysis.shared.helper.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Nạp driver SQL Server
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                String url = AppConfig.getDbUrl();
                String user = AppConfig.getDbUser();
                String pass = AppConfig.getDbPassword();

                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Kết nối SQL Server thành công!");
            } catch (ClassNotFoundException e) {
                System.err.println("Không tìm thấy Driver SQL Server!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}