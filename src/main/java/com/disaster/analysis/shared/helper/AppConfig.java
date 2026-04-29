package com.disaster.analysis.shared.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Xin lỗi, không tìm thấy file application.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Các hàm tiện ích lấy nhanh cấu hình DB
    public static String getDbUrl() { return getProperty("db.url"); }
    public static String getDbUser() { return getProperty("db.user"); }
    public static String getDbPassword() { return getProperty("db.password"); }
}