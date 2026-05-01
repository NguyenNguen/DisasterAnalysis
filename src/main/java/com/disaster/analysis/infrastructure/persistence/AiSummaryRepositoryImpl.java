package com.disaster.analysis.infrastructure.persistence;

import com.disaster.analysis.domain.contract.IAiSummaryRepository;
import com.disaster.analysis.domain.entity.AiSummary;
import com.disaster.analysis.infrastructure.database.DatabaseManager;

import java.sql.*;
import java.util.Optional;

/**
 * Lớp triển khai các thao tác lưu trữ và truy vấn bản tóm tắt AI (AiSummary).
 * Sử dụng SQL Server MERGE để tối ưu hóa việc cập nhật dữ liệu.
 */
public class AiSummaryRepositoryImpl implements IAiSummaryRepository {

    /**
     * Lưu bản tóm tắt AI. Sử dụng cơ chế MERGE (Upsert) để tự động quyết định
     * việc INSERT hoặc UPDATE dựa trên sự tồn tại của project_id.
     *
     * @param summary Đối tượng chứa dữ liệu tóm tắt từ AI.
     */
    @Override
    public void save(AiSummary summary) {
        // Câu lệnh MERGE giúp xử lý nguyên tử (atomic) việc kiểm tra và ghi dữ liệu
        String sql = "MERGE INTO AISummaries AS target " +
                "USING (SELECT ? AS pid) AS source ON target.project_id = source.pid " +
                "WHEN MATCHED THEN " +
                "  UPDATE SET summary_text = ?, generated_at = GETDATE(), posts_analyzed = ?, comments_analyzed = ?, model = ? " +
                "WHEN NOT MATCHED THEN " +
                "  INSERT (project_id, summary_text, posts_analyzed, comments_analyzed, model) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // --- Tham số cho phần MATCHED (UPDATE) và điều kiện ON ---
            pstmt.setLong(1, summary.getProjectId());
            pstmt.setString(2, summary.getSummaryText());
            pstmt.setInt(3, summary.getPostsAnalyzed());
            pstmt.setInt(4, summary.getCommentsAnalyzed());
            pstmt.setString(5, summary.getModel());

            // --- Tham số cho phần NOT MATCHED (INSERT) ---
            pstmt.setLong(6, summary.getProjectId());
            pstmt.setString(7, summary.getSummaryText());
            pstmt.setInt(8, summary.getPostsAnalyzed());
            pstmt.setInt(9, summary.getCommentsAnalyzed());
            pstmt.setString(10, summary.getModel());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Log lỗi truy vấn cơ sở dữ liệu
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm bản tóm tắt AI mới nhất của một dự án.
     *
     * @param projectId Mã ID của dự án.
     * @return Optional chứa AiSummary nếu tìm thấy, hoặc rỗng nếu chưa có tóm tắt.
     */
    @Override
    public Optional<AiSummary> findByProjectId(Long projectId) {
        String sql = "SELECT * FROM AISummaries WHERE project_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    AiSummary s = new AiSummary();
                    // Ánh xạ dữ liệu từ SQL sang Object
                    s.setSummaryText(rs.getString("summary_text"));
                    s.setModel(rs.getString("model"));

                    // Chuyển đổi kiểu dữ liệu thời gian
                    if (rs.getTimestamp("generated_at") != null) {
                        s.setGeneratedAt(rs.getTimestamp("generated_at").toLocalDateTime());
                    }
                    return Optional.of(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Xóa bản tóm tắt AI liên quan đến dự án.
     *
     * @param projectId Mã ID dự án cần xóa tóm tắt.
     */
    @Override
    public void deleteByProjectId(Long projectId) {
        String sql = "DELETE FROM AISummaries WHERE project_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}