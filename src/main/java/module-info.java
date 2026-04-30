/**
 * Định nghĩa module hệ thống phân tích thiên tai (DisasterAnalysisSystem).
 */
module com.disaster.analysis {
    // Khai báo các module phụ thuộc

    // Cung cấp các thành phần giao diện người dùng (Button, Label, TableView...)
    requires javafx.controls;

    // Hỗ trợ tải và quản lý các file giao diện định dạng FXML
    requires javafx.fxml;

    // Cung cấp API để làm việc với cơ sở dữ liệu (JDBC)
    requires java.sql;

    // Thư viện hỗ trợ chuyển đổi dữ liệu giữa Java Object và JSON
    requires com.google.gson;

    // Driver cụ thể để kết nối và tương tác với SQL Server
    requires com.microsoft.sqlserver.jdbc;

    // --- Cấu hình quyền truy cập (Encapsulation) ---

    // Cho phép module javafx.fxml truy cập các thành phần (cả private)
    // trong package controller thông qua Reflection để xử lý sự kiện giao diện.
    opens com.disaster.analysis.presentation.controller to javafx.fxml;

    // Công khai package chính để các module khác hoặc JVM có thể khởi chạy ứng dụng.
    exports com.disaster.analysis;
}