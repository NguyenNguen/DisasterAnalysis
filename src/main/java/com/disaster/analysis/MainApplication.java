package com.disaster.analysis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Lớp khởi chạy chính cho ứng dụng Disaster Relief Radar.
 * Kế thừa từ Application để khởi tạo môi trường JavaFX.
 */
public class MainApplication extends Application {

    /**
     * Điểm bắt đầu của vòng đời ứng dụng JavaFX.
     *
     * @param stage Cửa sổ chính (Primary Stage) của ứng dụng.
     * @throws Exception Các ngoại lệ xảy ra trong quá trình tải tệp FXML.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Khởi tạo FXMLLoader để đọc cấu hình giao diện từ file .fxml
        // Lưu ý: Đường dẫn file bắt đầu từ thư mục resources
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));

        // Tạo một Scene (khung cảnh) chứa toàn bộ giao diện đã tải, thiết lập kích thước 1000x700
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

        // Thiết lập tiêu đề hiển thị trên thanh tiêu đề của cửa sổ
        stage.setTitle("Disaster Relief Radar");

        // Gắn Scene vừa tạo vào Stage (cửa sổ)
        stage.setScene(scene);

        // Hiển thị cửa sổ lên màn hình người dùng
        stage.show();
    }

    /**
     * Phương thức main tiêu chuẩn của Java.
     * Gọi launch() để kích hoạt hệ thống JavaFX và phương thức start().
     */
    public static void main(String[] args) {
        launch();
    }
}