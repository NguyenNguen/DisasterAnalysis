package com.disaster.analysis.shared.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lớp hỗ trợ quản lý cấu hình ứng dụng.
 * Chịu trách nhiệm đọc các thông số từ file application.properties.
 * Đảm bảo tính Singleton: chỉ nạp file một lần duy nhất.
 */
public class AppConfig {

    // Đối tượng lưu trữ tập hợp các thuộc tính cấu hình dưới dạng key-value
    private static final Properties properties = new Properties();

    // Khối tĩnh (static block): Tự động thực thi một lần duy nhất khi lớp được nạp vào bộ nhớ
    static {
        // Sử dụng ClassLoader để tìm tệp cấu hình trong thư mục resources
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                // Thông báo nếu không tìm thấy file cấu hình để tránh lỗi NullPointerException sau này
                System.out.println("Xin lỗi, không tìm thấy file application.properties");
            } else {
                // Tải dữ liệu từ stream vào đối tượng properties
                properties.load(input);
            }
        }
        catch (IOException ex) {
            // In vết lỗi nếu có vấn đề trong quá trình đọc file (ví dụ: file bị hỏng)
            ex.printStackTrace();
        }
    }

    /**
     * Lấy giá trị cấu hình dựa trên khóa (key) tương ứng.
     *
     * @param key Tên của thuộc tính cần lấy.
     * @return Giá trị của thuộc tính dưới dạng String.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Lấy giá trị và trả về mặc định nếu không tìm thấy
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // Các phương thức tiện ích để lấy nhanh thông tin kết nối Database

    /** @return Đường dẫn kết nối cơ sở dữ liệu (JDBC URL) */
    public static String getDbUrl() {
        return getProperty("db.url");
    }

    /** @return Tên đăng nhập cơ sở dữ liệu */
    public static String getDbUser() {
        return getProperty("db.user");
    }

    /** @return Mật khẩu truy cập cơ sở dữ liệu */
    public static String getDbPassword() {
        return getProperty("db.password");
    }

    /** @return Chuỗi chứa mã khóa API của dịch vụ AI */
    public static String getAiKey() {
        return getProperty("ai.api.key");
    }
}