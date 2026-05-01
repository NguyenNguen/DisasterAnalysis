package com.disaster.analysis.infrastructure.persistence;

import com.disaster.analysis.domain.contract.ICommentRepository;
import com.disaster.analysis.domain.entity.Comment;
import com.disaster.analysis.infrastructure.database.DatabaseManager;
import com.disaster.analysis.shared.constant.Platform;
import com.disaster.analysis.shared.constant.Sentiment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp triển khai các thao tác lưu trữ và truy vấn bình luận (Comment) từ cơ sở dữ liệu.
 * Thực thi các phương thức được định nghĩa trong giao diện ICommentRepository.
 */
public class CommentRepositoryImpl implements ICommentRepository {

    /**
     * Lưu danh sách bình luận hàng loạt bằng cơ chế Batch Processing.
     * Sử dụng Transaction để đảm bảo tính toàn vẹn: Hoặc lưu tất cả, hoặc không lưu gì nếu có lỗi.
     *
     * @param comments Danh sách các đối tượng Comment cần lưu.
     */
    @Override
    public void saveAll(List<Comment> comments) {
        String sql = "INSERT INTO Comments (post_id, project_id, platform_id, platform, content, author, published_at, is_analyzed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            // Tắt Auto-commit để quản lý giao dịch (Transaction) thủ công
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Comment c : comments) {
                    pstmt.setLong(1, c.getPostId());
                    pstmt.setLong(2, c.getProjectId());
                    pstmt.setString(3, c.getPlatformId());
                    pstmt.setString(4, c.getPlatform().name());
                    pstmt.setString(5, c.getContent());
                    pstmt.setString(6, c.getAuthor());
                    pstmt.setTimestamp(7, Timestamp.valueOf(c.getPublishedAt()));
                    pstmt.setBoolean(8, c.isAnalyzed());

                    // Thêm lệnh vào hàng đợi Batch
                    pstmt.addBatch();
                }

                // Thực thi toàn bộ danh sách lệnh trong một lần gửi duy nhất
                pstmt.executeBatch();

                // Xác nhận hoàn tất giao dịch thành công
                conn.commit();
            } catch (SQLException e) {
                // Hoàn tác mọi thay đổi nếu có bất kỳ lỗi nào xảy ra trong Batch
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm toàn bộ bình luận thuộc về một bài đăng cụ thể.
     *
     * @param postId ID của bài đăng.
     * @return Danh sách các bình luận tìm thấy.
     */
    @Override
    public List<Comment> findByPostId(Long postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM Comments WHERE post_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapResultSetToComment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    /**
     * Truy xuất tất cả bình luận trong một dự án (truy vấn tắt qua project_id).
     * Giúp lấy dữ liệu thống kê nhanh mà không cần JOIN qua bảng bài đăng.
     *
     * @param projectId ID của dự án thiên tai.
     * @return Danh sách bình luận thuộc dự án.
     */
    @Override
    public List<Comment> findByProjectId(Long projectId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM Comments WHERE project_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapResultSetToComment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    /**
     * Xóa sạch các bình luận liên quan đến một bài đăng.
     *
     * @param postId ID bài đăng có bình luận cần xóa.
     */
    @Override
    public void deleteByPostId(Long postId) {
        String sql = "DELETE FROM Comments WHERE post_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, postId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức nội bộ để ánh xạ dòng dữ liệu từ SQL ResultSet sang đối tượng Comment.
     *
     * @param rs ResultSet tại vị trí hiện tại.
     * @return Đối tượng Comment hoàn chỉnh.
     * @throws SQLException Nếu có lỗi khi truy xuất dữ liệu từ các cột.
     */
    private Comment mapResultSetToComment(ResultSet rs) throws SQLException {
        Comment c = new Comment();
        c.setId(rs.getLong("id"));
        c.setPostId(rs.getLong("post_id"));
        c.setProjectId(rs.getLong("project_id"));
        c.setPlatformId(rs.getString("platform_id"));
        c.setPlatform(Platform.valueOf(rs.getString("platform")));
        c.setContent(rs.getString("content"));
        c.setAuthor(rs.getString("author"));

        // Chuyển đổi Timestamp từ DB sang LocalDateTime
        c.setPublishedAt(rs.getTimestamp("published_at").toLocalDateTime());

        // Xử lý giá trị Enum có thể Null (Sentiment)
        String sent = rs.getString("sentiment");
        if (sent != null) {
            c.setSentiment(Sentiment.valueOf(sent));
        }

        c.setDamageCategories(rs.getString("damage_categories"));
        c.setAnalyzed(rs.getBoolean("is_analyzed"));
        return c;
    }
}