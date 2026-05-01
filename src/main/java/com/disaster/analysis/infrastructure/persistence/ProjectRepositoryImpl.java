package com.disaster.analysis.infrastructure.persistence;

import com.disaster.analysis.domain.contract.IProjectRepository;
import com.disaster.analysis.domain.entity.Project;
import com.disaster.analysis.infrastructure.database.DatabaseManager;
import com.disaster.analysis.shared.constant.ProjectStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Lớp triển khai các thao tác truy xuất dữ liệu Dự án (Project) từ SQL Server.
 * Thực thi các quy tắc được định nghĩa trong giao diện IProjectRepository.
 */
public class ProjectRepositoryImpl implements IProjectRepository {

    /**
     * Lưu một dự án mới vào cơ sở dữ liệu.
     *
     * @param project Đối tượng dự án cần lưu trữ.
     * @return ID tự sinh từ database nếu thành công, ngược lại trả về null.
     */
    @Override
    public Long save(Project project) {
        String sql = "INSERT INTO Projects (name, disaster_name, keywords, hashtags, start_date, end_date, platforms, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Sử dụng try-with-resources để đảm bảo đóng kết nối và giải phóng tài nguyên
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getDisasterName());
            pstmt.setString(3, project.getKeywords());
            pstmt.setString(4, project.getHashtags());
            pstmt.setTimestamp(5, Timestamp.valueOf(project.getStartDate()));
            pstmt.setTimestamp(6, Timestamp.valueOf(project.getEndDate()));
            pstmt.setString(7, project.getPlatforms());
            pstmt.setString(8, project.getStatus().name());

            pstmt.executeUpdate();

            // Lấy ID tự sinh (Identity/Auto-increment) từ SQL Server
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin của một dự án đã có.
     *
     * @param project Đối tượng dự án chứa các thông tin mới cần cập nhật.
     */
    @Override
    public void update(Project project) {
        String sql = "UPDATE Projects SET name=?, disaster_name=?, keywords=?, hashtags=?, status=?, last_modified=GETDATE() WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getDisasterName());
            pstmt.setString(3, project.getKeywords());
            pstmt.setString(4, project.getHashtags());
            pstmt.setString(5, project.getStatus().name());
            pstmt.setLong(6, project.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa một dự án khỏi hệ thống theo ID.
     *
     * @param id Mã định danh của dự án cần xóa.
     */
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM Projects WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm thông tin chi tiết của một dự án theo ID.
     *
     * @param id Mã định danh dự án.
     * @return Optional chứa đối tượng Project nếu tìm thấy, hoặc rỗng nếu không có dữ liệu.
     */
    @Override
    public Optional<Project> findById(Long id) {
        String sql = "SELECT * FROM Projects WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProject(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Lấy toàn bộ danh sách các dự án trong hệ thống.
     *
     * @return Danh sách các đối tượng Project, sắp xếp theo thời gian tạo giảm dần.
     */
    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM Projects ORDER BY created_at DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    /**
     * Phương thức trợ giúp để ánh xạ dữ liệu từ ResultSet sang đối tượng Project.
     * Giúp tái sử dụng mã nguồn và dễ bảo trì khi cấu trúc bảng thay đổi.
     *
     * @param rs ResultSet chứa dòng dữ liệu hiện tại.
     * @return Đối tượng Project đã được điền đầy đủ thông tin.
     * @throws SQLException Nếu có lỗi khi truy xuất tên cột hoặc kiểu dữ liệu.
     */
    private Project mapResultSetToProject(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setDisasterName(rs.getString("disaster_name"));
        p.setKeywords(rs.getString("keywords"));
        p.setHashtags(rs.getString("hashtags"));

        // Chuyển đổi từ Timestamp của SQL sang LocalDateTime của Java
        p.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        p.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());

        p.setPlatforms(rs.getString("platforms"));
        p.setStatus(ProjectStatus.valueOf(rs.getString("status")));
        return p;
    }
}