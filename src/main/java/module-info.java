module com.disaster.analysis {
    // Khai báo các module cần dùng
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires com.microsoft.sqlserver.jdbc;

    // Cho phép JavaFX truy cập vào các Controller để điều khiển giao diện
    opens com.disaster.analysis.presentation.controller to javafx.fxml;

    // Xuất package chính ra để chạy ứng dụng
    exports com.disaster.analysis;
}