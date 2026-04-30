package com.disaster.analysis.domain.contract;

/**
 * Giao diện định nghĩa các dịch vụ tương tác với Trí tuệ nhân tạo (AI).
 * Cung cấp các phương thức để xử lý ngôn ngữ tự nhiên và tóm tắt dữ liệu.
 */
public interface IAIClient {

    /**
     * Gửi một yêu cầu (prompt) tới mô hình AI và nhận về văn bản tóm tắt.
     * Thường được sử dụng để tổng hợp nội dung từ hàng ngàn bài đăng thu thập được.
     *
     * @param prompt Nội dung hướng dẫn và dữ liệu đầu vào cho AI.
     * @return Chuỗi văn bản tóm tắt hoặc phản hồi được tạo ra từ AI.
     */
    String generateSummary(String prompt);
}