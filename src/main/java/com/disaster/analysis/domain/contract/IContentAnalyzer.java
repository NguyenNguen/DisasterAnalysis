package com.disaster.analysis.domain.contract;

import com.disaster.analysis.shared.constant.Sentiment;
import java.util.List;

/**
 * Giao diện định nghĩa các bộ lọc và phân tích nội dung văn bản.
 * Chịu trách nhiệm trích xuất ý nghĩa ngữ nghĩa và phân loại dữ liệu từ các báo cáo thiên tai.
 */
public interface IContentAnalyzer {

    /**
     * Phân tích sắc thái cảm xúc của một đoạn văn bản.
     *
     * @param text Đoạn văn bản cần phân tích (ví dụ: một bài đăng trên mạng xã hội).
     * @return Đối tượng {@link Sentiment} (TÍCH CỰC, TIÊU CỰC, TRUNG LẬP) thể hiện thái độ của người viết.
     */
    Sentiment analyzeSentiment(String text);

    /**
     * Nhận diện và phân loại các loại hình thiệt hại được nhắc đến trong văn bản.
     *
     * @param text Nội dung báo cáo hoặc tin tức về thiên tai.
     * @return Danh sách các chuỗi (List of Strings) đại diện cho các danh mục thiệt hại
     *         (ví dụ: "Nhà cửa", "Cầu đường", "Người mất tích").
     */
    List<String> analyzeDamageCategories(String text);
}