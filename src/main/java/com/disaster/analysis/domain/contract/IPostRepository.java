package com.disaster.analysis.domain.contract;

import com.disaster.analysis.domain.entity.Post;
import com.disaster.analysis.shared.constant.Sentiment;
import java.util.List;
import java.util.Map;

/**
 * Giao diện quản lý dữ liệu cho các bài đăng (Post) thu thập được.
 * Cung cấp các phương thức lưu trữ và truy vấn phục vụ việc phân tích dữ liệu thiên tai.
 */
public interface IPostRepository {

    /**
     * Lưu hàng loạt danh sách bài đăng vào hệ thống.
     * Thường được sử dụng sau khi quét dữ liệu từ mạng xã hội hoặc API.
     *
     * @param posts Danh sách các đối tượng Post cần lưu trữ.
     */
    void saveAll(List<Post> posts);

    /**
     * Tìm kiếm toàn bộ bài đăng liên quan đến một dự án cụ thể.
     *
     * @param projectId Mã định danh của dự án.
     * @return Danh sách các bài đăng thuộc về dự án đó.
     */
    List<Post> findByProjectId(Long projectId);

    // Các phương thức hỗ trợ thống kê và trực quan hóa dữ liệu

    /**
     * Thống kê số lượng bài đăng dựa trên sắc thái cảm xúc (Sentiment) trong một dự án.
     * Dữ liệu này thường được dùng để vẽ biểu đồ tròn (Pie Chart) về tâm lý dư luận.
     *
     * @param projectId Mã định danh của dự án cần thống kê.
     * @return Một Map chứa cặp khóa-giá trị (Sắc thái cảm xúc : Số lượng bài đăng).
     */
    Map<Sentiment, Integer> countSentimentByProject(Long projectId);

    /**
     * Thống kê số lượng bài đăng dựa trên các loại thiệt hại (Damage) trong một dự án.
     * Dữ liệu này thường được dùng để vẽ biểu đồ cột (Bar Chart) đánh giá mức độ ảnh hưởng.
     *
     * @param projectId Mã định danh của dự án cần thống kê.
     * @return Một Map chứa cặp khóa-giá trị (Tên loại thiệt hại : Số lượng ghi nhận).
     */
    Map<String, Integer> countDamageByProject(Long projectId);
}