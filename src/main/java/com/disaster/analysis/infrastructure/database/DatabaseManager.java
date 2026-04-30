package com.disaster.analysis.infrastructure.database;

import com.disaster.analysis.shared.helper.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp quản lý kết nối cơ sở dữ liệu (Đóng/mở).
 * Sử dụng mẫu thiết kế Singleton để duy trì một kết nối duy nhất tới SQL Server.
 */
public class DatabaseManager {

    // Biến lưu trữ đối tượng kết nối duy nhất
    private static Connection connection = null;

    /**
     * Thiết lập và trả về kết nối tới cơ sở dữ liệu.
     * Nếu kết nối chưa tồn tại hoặc đã bị đóng, phương thức sẽ khởi tạo kết nối mới.
     *
     * @return Đối tượng Connection tới SQL Server.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình kết nối.
     */
    public static Connection getConnection() throws SQLException {
        // Kiểm tra nếu kết nối chưa được tạo hoặc đã bị đóng trước đó
        if (connection == null || connection.isClosed()) {
            try {
                // Đăng ký Driver SQL Server với JDBC (Cần thiết cho các phiên bản Java cũ)
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Lấy thông tin cấu hình từ lớp tiện ích AppConfig
                String url = AppConfig.getDbUrl();
                String user = AppConfig.getDbUser();
                String pass = AppConfig.getDbPassword();

                // Thực hiện mở kết nối thông qua DriverManager
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Kết nối SQL Server thành công!");

            } catch (ClassNotFoundException e) {
                // Lỗi xảy ra khi thư viện Driver JDBC chưa được thêm vào classpath
                System.err.println("Không tìm thấy Driver SQL Server! Hãy kiểm tra file module-info hoặc thư viện Maven.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Đóng kết nối cơ sở dữ liệu khi ứng dụng không còn sử dụng.
     * Giúp giải phóng tài nguyên hệ thống và tránh tình trạng treo kết nối (leak).
     */
    public static void closeConnection() {
        try {
            // Chỉ đóng nếu kết nối đang tồn tại và vẫn còn đang mở
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối cơ sở dữ liệu.");
            }
        } catch (SQLException e) {
            // In vết lỗi nếu quá trình đóng kết nối gặp sự cố
            e.printStackTrace();
        }
    }
}