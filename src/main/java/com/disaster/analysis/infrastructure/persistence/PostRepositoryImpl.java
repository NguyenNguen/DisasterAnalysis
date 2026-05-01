package com.disaster.analysis.infrastructure.persistence;

import com.disaster.analysis.domain.contract.IPostRepository;
import com.disaster.analysis.domain.entity.Post;
import com.disaster.analysis.infrastructure.database.DatabaseManager;
import com.disaster.analysis.shared.constant.Platform;
import com.disaster.analysis.shared.constant.Sentiment;

import java.sql.*;
import java.util.*;

/**
 * Lớp triển khai các thao tác lưu trữ và truy vấn bài đăng (Post).
 * Quản lý việc lưu dữ liệu hàng loạt và thống kê dữ liệu phục vụ báo cáo.
 */
public class PostRepositoryImpl implements IPostRepository {

    /**
     * Lưu danh sách bài đăng theo lô (Batch Processing) lồng trong một giao dịch (Transaction).
     * Đảm bảo tất cả bài đăng được lưu thành công hoặc không có bài nào được lưu nếu xảy ra lỗi.
     *
     * @param posts Danh sách các đối tượng Post cần lưu trữ.
     */
    @Override
    public void saveAll(List<Post> posts) {
        String sql = "INSERT INTO Posts (project_id, platform_id, platform, content, author, published_at, url, is_analyzed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            // Tắt chế độ tự động xác nhận để bắt đầu một Transaction thủ công
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Post post : posts) {
                    pstmt.setLong(1, post.getProjectId());
                    pstmt.setString(2, post.getPlatformId());
                    pstmt.setString(3, post.getPlatform().name());
                    pstmt.setString(4, post.getContent());
                    pstmt.setString(5, post.getAuthor());
                    pstmt.setTimestamp(6, Timestamp.valueOf(post.getPublishedAt()));
                    pstmt.setString(7, post.getUrl());
                    pstmt.setBoolean(8, post.isAnalyzed());

                    // Thêm câu lệnh vào hàng đợi bộ nhớ đệm
                    pstmt.addBatch();
                }

                // Gửi toàn bộ hàng đợi xuống SQL Server trong một lần kết nối duy nhất
                pstmt.executeBatch();

                // Xác nhận lưu dữ liệu vĩnh viễn vào ổ đĩa
                conn.commit();

            } catch (SQLException e) {
                // Nếu có bất kỳ lỗi nào, hoàn tác (Rollback) mọi thay đổi để tránh dữ liệu rác
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách bài đăng thuộc một dự án, sắp xếp theo thời gian mới nhất.
     *
     * @param projectId ID của dự án cần truy vấn.
     * @return Danh sách Post đã được ánh xạ từ Database.
     */
    @Override
    public List<Post> findByProjectId(Long projectId) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM Posts WHERE project_id = ? ORDER BY published_at DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Thống kê số lượng bài đăng theo từng loại sắc thái cảm xúc.
     * Dữ liệu trả về được sử dụng trực tiếp để vẽ biểu đồ tròn (PieChart).
     *
     * @param projectId ID của dự án.
     * @return Map với Key là Sentiment và Value là tổng số lượng tương ứng.
     */
    @Override
    public Map<Sentiment, Integer> countSentimentByProject(Long projectId) {
        Map<Sentiment, Integer> stats = new HashMap<>();
        String sql = "SELECT sentiment, COUNT(*) as total FROM Posts " +
                "WHERE project_id = ? AND sentiment IS NOT NULL GROUP BY sentiment";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    stats.put(Sentiment.valueOf(rs.getString("sentiment")), rs.getInt("total"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * Thống kê số lượng bài đăng theo các danh mục thiệt hại.
     * Vì một bài đăng có thể có nhiều danh mục (phân tách bởi dấu phẩy),
     * phương thức này sẽ bóc tách và đếm thủ công.
     *
     * @param projectId ID của dự án.
     * @return Map chứa nhãn thiệt hại và số lượng ghi nhận.
     */
    @Override
    public Map<String, Integer> countDamageByProject(Long projectId) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT damage_categories FROM Posts WHERE project_id = ? AND damage_categories IS NOT NULL";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String rawCats = rs.getString("damage_categories");
                    // Xử lý chuỗi nhãn (Ví dụ: "Hạ tầng, Nhân mạng") thành các phần tử riêng biệt
                    for (String cat : rawCats.split(",")) {
                        String trimmedCat = cat.trim();
                        if (!trimmedCat.isEmpty()) {
                            // Tăng số đếm cho nhãn hiện tại
                            stats.put(trimmedCat, stats.getOrDefault(trimmedCat, 0) + 1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * Ánh xạ dữ liệu từ ResultSet sang đối tượng Post.
     *
     * @param rs ResultSet tại dòng hiện tại.
     * @return Đối tượng Post hoàn chỉnh.
     * @throws SQLException Nếu có lỗi khi đọc cột dữ liệu.
     */
    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        Post p = new Post();
        p.setId(rs.getLong("id"));
        p.setProjectId(rs.getLong("project_id"));
        p.setPlatformId(rs.getString("platform_id"));
        p.setPlatform(Platform.valueOf(rs.getString("platform")));
        p.setContent(rs.getString("content"));
        p.setAuthor(rs.getString("author"));
        p.setPublishedAt(rs.getTimestamp("published_at").toLocalDateTime());
        p.setUrl(rs.getString("url"));
        p.setAnalyzed(rs.getBoolean("is_analyzed"));

        // Đọc thêm các trường metadata nếu có (ví dụ: sentiment)
        String sentimentStr = rs.getString("sentiment");
        if (sentimentStr != null) {
            p.setSentiment(Sentiment.valueOf(sentimentStr));
        }

        return p;
    }
}